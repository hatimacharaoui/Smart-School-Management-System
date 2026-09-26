package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.DevoirDto;
import com.smartschool.backend.entity.Devoir;
import com.smartschool.backend.entity.Role;
import com.smartschool.backend.entity.StatutDevoir;
import com.smartschool.backend.entity.TypeNotification;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.DevoirRepository;
import com.smartschool.backend.service.DevoirService;
import com.smartschool.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DevoirServiceImpl implements DevoirService {

    private final DevoirRepository devoirRepository;
    private final NotificationService notificationService;
    private final Mappers mappers;


    public Page<DevoirDto> chercherParClasse(Long classeId, String recherche, StatutDevoir statut, Pageable pageable) {
        return devoirRepository.rechercher(recherche, statut, null, classeId, pageable)
                .map(mappers::toDto);
    }

    public Page<DevoirDto> afficher(String recherche, StatutDevoir statut, Pageable pagination) {
        return devoirRepository.findAll(pagination).map(mappers::toDto);
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
            notificationService.notifierRole(
                    Role.ENSEIGNANT,
                    "Devoir en correction",
                    "Un devoir est prêt à être corrigé.",
                    TypeNotification.DEVOIR,
                    id
            );
        } else if (statut == StatutDevoir.CORRIGE) {
            notificationService.notifierRole(
                    Role.ELEVE,
                    "Devoir corrigé",
                    "Votre devoir a été corrigé.",
                    TypeNotification.DEVOIR,
                    id
            );
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
