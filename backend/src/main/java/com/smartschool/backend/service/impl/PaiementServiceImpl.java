package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.PaiementDto;
import com.smartschool.backend.entity.*;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.EleveRepository;
import com.smartschool.backend.repository.PaiementRepository;
import com.smartschool.backend.repository.UserRepository;
import com.smartschool.backend.service.NotificationService;
import com.smartschool.backend.service.PaiementService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaiementServiceImpl implements PaiementService {
    private static final double MONTANT_MENSUEL_PAR_DEFAUT = 1500;
    private static final String METHODE_PAR_DEFAUT = "Espèces";

    private final PaiementRepository paiementRepository;
    private final EleveRepository eleveRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final Mappers mappers;



    public Page<PaiementDto> afficher(StatutPaiement statut, LocalDate dateDebut, LocalDate dateFin,Pageable pagination) {

        return paiementRepository.rechercher(null, statut, dateDebut, dateFin, pagination)
                .map(mappers::toDto);
    }

    public PaiementDto chercherParId(Long id) {
        Paiement paiement = findById(id);
        verifierAccesParent(paiement.getParentId());
        return mappers.toDto(paiement);
    }

    public Page<PaiementDto> chercherParEleve(Long eleveId, StatutPaiement statut, LocalDate dateDebut, LocalDate dateFin, Pageable pagination) {

        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new RuntimeException("Elève introuvable"));
        verifierAccesParent(eleve.getParentId());
        return paiementRepository.rechercher(eleveId, statut, dateDebut, dateFin, pagination).map(mappers::toDto);
    }

    public Page<PaiementDto> listerMensuels(
            int annee,
            int mois,
            StatutPaiement statut,
            Pageable pagination
    ) {
        if (annee < 2020 || annee > 2100 || mois < 1 || mois > 12) {
            throw new IllegalArgumentException("Le mois demandé est invalide.");
        }

        LocalDate dateDebut = LocalDate.of(annee, mois, 1);
        LocalDate dateFin = dateDebut.plusMonths(1).minusDays(1);
        Page<Eleve> pageEleves = eleveRepository.chercherParStatutPaiementMensuel(
                statut,
                StatutPaiement.EN_ATTENTE,
                dateDebut,
                dateFin,
                pagination
        );
        List<Long> eleveIds = new ArrayList<>();
        for (Eleve eleve : pageEleves.getContent()) {
            eleveIds.add(eleve.getId());
        }

        List<Paiement> paiements = new ArrayList<>();
        if (!eleveIds.isEmpty()) {
            paiements = paiementRepository.findByEleveIdInAndDateBetween(
                    eleveIds,
                    dateDebut,
                    dateFin
            );
        }

        List<Paiement> paiementsDuMois = paiements;
        return pageEleves.map(
                eleve -> construirePaiementMensuel(eleve, paiementsDuMois, dateDebut)
        );
    }

    @CacheEvict(value = "paiements", allEntries = true)
    public PaiementDto creer(PaiementDto dto) {
        verifierCreation(dto);
        LocalDate debutMois = dto.getDate().withDayOfMonth(1);
        LocalDate finMois = debutMois.plusMonths(1).minusDays(1);
        if (paiementRepository.existsByEleveIdAndDateBetween(
                dto.getEleveId(),
                debutMois,
                finMois
        )) {
            throw new IllegalArgumentException(
                    "Un paiement existe déjà pour cet élève et ce mois."
            );
        }
        Paiement paiement = mappers.toEntite(dto);
        return mappers.toDto(paiementRepository.save(paiement));
    }

    @CacheEvict(value = "paiements", allEntries = true)
    public PaiementDto modifier(Long id, PaiementDto dto) {
        verifierMontantEtMethode(dto);
        Paiement paiement = findById(id);
        paiement.setMontant(dto.getMontant());
        paiement.setMethode(dto.getMethode());
        return mappers.toDto(paiementRepository.save(paiement));
    }

    private void verifierCreation(PaiementDto dto) {
        verifierMontantEtMethode(dto);
        User user = utilisateurConnecte();

        Eleve eleve = eleveRepository.findById(dto.getEleveId())
                .orElseThrow(() -> new RuntimeException("Élève introuvable"));
        if (!Objects.equals(dto.getParentId(), eleve.getParentId())) {
            throw new IllegalArgumentException(
                    "Le parent sélectionné n'est pas lié à cet élève."
            );
        }

        if (user.getRole() == Role.PARENT) {
            if (!Objects.equals(user.getId(), dto.getParentId())) {
                throw new IllegalArgumentException(
                        "Vous ne pouvez créer un paiement que pour votre enfant."
                );
            }
            dto.setStatut(StatutPaiement.EN_ATTENTE);
        }
    }

    private void verifierAccesParent(Long parentId) {
        User user = utilisateurConnecte();
        if (user.getRole() == Role.PARENT && !Objects.equals(user.getId(), parentId)) {
            throw new IllegalArgumentException("Ce paiement ne concerne pas votre compte.");
        }
    }

    private User utilisateurConnecte() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalArgumentException("Utilisateur non authentifié.");
        }
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    private void verifierMontantEtMethode(PaiementDto dto) {
        if (dto.getMontant() <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif.");
        }
        if (!METHODE_PAR_DEFAUT.equals(dto.getMethode())
                && !"Virement bancaire".equals(dto.getMethode())) {
            throw new IllegalArgumentException(
                    "La méthode doit être Espèces ou Virement bancaire."
            );
        }
    }

    private PaiementDto construirePaiementMensuel(
            Eleve eleve,
            List<Paiement> paiements,
            LocalDate dateMois
    ) {
        for (Paiement paiement : paiements) {
            if (Objects.equals(paiement.getEleveId(), eleve.getId())) {
                return mappers.toDto(paiement);
            }
        }

        return PaiementDto.builder()
                .eleveId(eleve.getId())
                .parentId(eleve.getParentId())
                .montant(MONTANT_MENSUEL_PAR_DEFAUT)
                .methode(METHODE_PAR_DEFAUT)
                .date(dateMois)
                .statut(StatutPaiement.EN_ATTENTE)
                .build();
    }

    @CacheEvict(value = "paiements", allEntries = true)
    public PaiementDto modifierStatut(Long id, StatutPaiement statut) {
        Paiement paiement = findById(id);
        paiement.setStatut(statut);
        Paiement paiementEnregistre = paiementRepository.save(paiement);
        notificationService.notifierRole(
                Role.PARENT,
                "Statut du paiement",
                "Le statut du paiement est maintenant : " + libelleStatut(statut) + ".",
                TypeNotification.PAIEMENT,
                id
        );
        return mappers.toDto(paiementEnregistre);
    }

    private String libelleStatut(StatutPaiement statut) {
        if (statut == StatutPaiement.VALIDE) {
            return "Validé";
        } else if (statut == StatutPaiement.REFUSE) {
            return "Refusé";
        } else {
            return "En attente";
        }
    }

    private Paiement findById(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement introuvalbe"));
    }
}
