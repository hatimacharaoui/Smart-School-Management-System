package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.DevoirDto;
import com.smartschool.backend.entity.*;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.DevoirRepository;
import com.smartschool.backend.repository.EleveRepository;
import com.smartschool.backend.service.DevoirService;
import com.smartschool.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DevoirServiceImpl implements DevoirService {

    private final DevoirRepository devoirRepository;
    private final EleveRepository eleveRepository;
    private final NotificationService notificationService;
    private final Mappers mappers;


    public Page<DevoirDto> chercherParClasse(Long classeId, String recherche, StatutDevoir statut, Pageable pageable) {
        return devoirRepository.rechercher(recherche, statut, null, classeId, pageable)
                .map(mappers::toDto);
    }

    public Page<DevoirDto> afficher(String recherche, StatutDevoir statut, Pageable pageable) {
        return devoirRepository.rechercher(recherche, statut, null, null, pageable).map(mappers::toDto);
    }

    @Cacheable(value = "devoirs", key = "'id-' + #id")
    public DevoirDto chercherParId(Long id) {

        return mappers.toDto(findById(id));
    }

    @CacheEvict(value = "devoirs", allEntries = true)
    public DevoirDto creer(DevoirDto dto) {
        Devoir devoir = mappers.toEntite(dto);
        Devoir devoirEnregistre = devoirRepository.save(devoir);
        notificationService.notifierRole(
                Role.ELEVE,
                "Nouveau devoir",
                "Un nouveau devoir a été créé.",
                TypeNotification.DEVOIR,
                devoirEnregistre.getId()
        );
        notificationService.notifierRole(
                Role.PARENT,
                "Nouveau devoir",
                "Un nouveau devoir concerne votre enfant.",
                TypeNotification.DEVOIR,
                devoirEnregistre.getId()
        );
        return mappers.toDto(devoirEnregistre);
    }

    @CacheEvict(value = "devoirs", allEntries = true)
    public DevoirDto modifier(Long id, DevoirDto devoirDto) {
        Devoir devoir = findById(id);
        mappers.update(devoirDto, devoir);

        return mappers.toDto(devoirRepository.save(devoir));
    }

    @CacheEvict(value = "devoirs", allEntries = true)
    public void supprimer(Long id) {
        Devoir devoir = findById(id);
        devoirRepository.delete(devoir);

    }

    @CacheEvict(value = "devoirs", allEntries = true)
    public DevoirDto modifierStatut(Long id, StatutDevoir statut) {

        Devoir devoir = findById(id);
        devoir.setStatut(statut);
        Devoir devoirEnregistre = devoirRepository.save(devoir);

        if (statut == StatutDevoir.EN_CORRECTION) {

            notificationService.notifierUtilisateur(
                    devoir.getEnseignantId(),
                    "Devoir en correction",
                    "Le devoir \"" + devoir.getTitre() + "\" est prêt à être corrigé.",
                    TypeNotification.DEVOIR,
                    devoir.getId()
            );

        } else if (statut == StatutDevoir.CORRIGE) {

            List<Eleve> eleves =
                    eleveRepository.findByClasseId(devoir.getClasseId());

            for (Eleve eleve : eleves) {

                notificationService.notifierUtilisateur(
                        eleve.getId(),
                        "Devoir corrigé",
                        "Le devoir \"" + devoir.getTitre() + "\" a été corrigé.",
                        TypeNotification.DEVOIR,
                        devoir.getId()
                );

                notificationService.notifierUtilisateur(
                        eleve.getParentId(),
                        "Devoir corrigé",
                        "Le devoir \"" + devoir.getTitre() + "\" de votre enfant a été corrigé.",
                        TypeNotification.DEVOIR,
                        devoir.getId()
                );}
        }
        return mappers.toDto(devoirEnregistre);
    }


    public Page<DevoirDto> chercherParEnseignant(Long enseignantId, String recherche, StatutDevoir statut, Pageable pagination) {

        return devoirRepository.rechercher(recherche, statut, enseignantId, null, pagination)
                .map(mappers::toDto);
    }

    private Devoir findById(Long id) {
        return devoirRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Devoir introuvable"));
    }
}
