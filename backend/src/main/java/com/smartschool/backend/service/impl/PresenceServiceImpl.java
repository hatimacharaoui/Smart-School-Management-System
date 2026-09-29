package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.PresenceDto;
import com.smartschool.backend.entity.*;
import com.smartschool.backend.exception.ResourceNotFoundException;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.EleveRepository;
import com.smartschool.backend.repository.PresenceRepository;
import com.smartschool.backend.service.NotificationService;
import com.smartschool.backend.service.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.core.support.RepositoryMethodInvocationListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PresenceServiceImpl implements PresenceService {

    private final PresenceRepository presenceRepository;
    private final NotificationService notificationService;
    private final EleveRepository eleveRepository;
    private final Mappers mappers;


    public Page<PresenceDto> chercherParEleve(Long eleveId, Pageable pageable) {
        return presenceRepository.findByEleveId(eleveId, pageable)
                .map(mappers::toDto);
    }

    public Page<PresenceDto> afficherPresence(Pageable pagination) {
        return presenceRepository.findByStatutIn(List.of(StatutPresence.EN_RETARD, StatutPresence.ABSENT),pagination)
                .map(mappers::toDto);
    }

    public Page<PresenceDto> chercherParClasseEtDate(Long classeId, LocalDate date, Pageable pagination) {

        return presenceRepository.findByClasseIdAndDate(classeId, date, pagination)
                .map(mappers::toDto);
    }

    @CacheEvict(value = "presences", allEntries = true)
    public PresenceDto enregistrer(PresenceDto dto) {
        Presence presence = mappers.toEntite(dto);
        Presence presenceEnregistree = enregistrerPresence(presence);
        return mappers.toDto(presenceEnregistree);
    }

    private Presence enregistrerPresence(Presence presence) {
        /*check presence de l'etudiant de chaque matiere et met update si exist*/
        Optional<Presence> presenceExistante = presenceRepository
                .findByEleveIdAndDateAndMatiereId(presence.getEleveId(), presence.getDate(), presence.getMatiereId());
        if (presenceExistante.isPresent()) {
            presence.setId(presenceExistante.get().getId());
        }
        Presence presenceEnregistree = presenceRepository.save(presence);

        if (presenceEnregistree.getStatut() == StatutPresence.ABSENT
                || presenceEnregistree.getStatut() == StatutPresence.EN_RETARD) {
            notificationService.notifierRole(
                    Role.ADMINISTRATEUR,
                    "Alerte de présence",
                    "Un élève est absent ou en retard.",
                    TypeNotification.PRESENCE,
                    presenceEnregistree.getId()
            );
            Eleve eleve = eleveRepository
                    .findById(presenceEnregistree.getEleveId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Élève introuvable ")
                    );
            notificationService.notifierUtilisateur(
                    eleve.getParentId(),
                    "Alerte de présence",
                    "Votre enfant est absent ou en retard ",
                    TypeNotification.PRESENCE,
                    presenceEnregistree.getId()
            );
        }
        return presenceEnregistree;
    }

    @CacheEvict(value = "presences", allEntries = true)
    public List<PresenceDto> enregistrerTout(List<PresenceDto> presences) {
        List<PresenceDto> resultats = new ArrayList<>();
        for (PresenceDto dto : presences) {
            Presence presence = mappers.toEntite(dto);
            Presence resultat = enregistrerPresence(presence);
            resultats.add(mappers.toDto(resultat));
        }
        return resultats;
    }

}
