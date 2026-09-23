package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.PresenceDto;
import com.smartschool.backend.entity.Presence;
import com.smartschool.backend.entity.StatutPresence;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.PresenceRepository;
import com.smartschool.backend.service.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.core.support.RepositoryMethodInvocationListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PresenceServiceImpl implements PresenceService {

    private final PresenceRepository presenceRepository;
    private final Mappers mappers;


    public Page<PresenceDto> chercherParEleve(Long eleveId, Pageable pageable) {
        return presenceRepository.findByEleveId(eleveId, pageable)
                .map(mappers::toDto);
    }

    public Page<PresenceDto> afficherPresence(Pageable pagination) {
        return presenceRepository.findByStatutIn(List.of(StatutPresence.EN_RETARD, StatutPresence.ABSENT),pagination)
                .map(mappers::toDto);
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
