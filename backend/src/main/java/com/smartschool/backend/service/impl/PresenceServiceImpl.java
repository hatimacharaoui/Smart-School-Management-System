package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.PresenceDto;
import com.smartschool.backend.entity.Presence;
import com.smartschool.backend.entity.Role;
import com.smartschool.backend.entity.StatutPresence;
import com.smartschool.backend.entity.TypeNotification;
import com.smartschool.backend.mapper.Mappers;
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
        Presence presenceEnregistree = enregistrerEntite(presence);
        return mappers.toDto(presenceEnregistree);
    }

    private Presence enregistrerEntite(Presence presence) {
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
            notificationService.notifierRole(
                    Role.PARENT,
                    "Alerte de présence",
                    "Votre enfant est absent ou en retard.",
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
            Presence resultat = enregistrerEntite(presence);
            resultats.add(mappers.toDto(resultat));
        }
        return resultats;
    }

    public PresenceDto creer(PresenceDto dto) {
        Presence presence = mappers.toEntite(dto);

        return mappers.toDto(presenceRepository.save(presence));
    }


    public PresenceDto modifier(Long id, PresenceDto dto) {
        Presence presence = findById(id);
        mappers.update(dto, presence);

        return mappers.toDto(presenceRepository.save(presence));
    }


    public void supprimer(Long id) {
        presenceRepository.delete(findById(id));
    }

    private Presence findById(Long id) {
        return presenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prensence introuvable"));
    }
}
