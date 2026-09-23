package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.DevoirDto;
import com.smartschool.backend.entity.Devoir;
import com.smartschool.backend.entity.StatutDevoir;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.DevoirRepository;
import com.smartschool.backend.service.DevoirService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DevoirServiceImpl implements DevoirService {

    private final DevoirRepository devoirRepository;
    private final Mappers mappers;


    public Page<DevoirDto> chercherParClasse(Long classeId, String recherche, StatutDevoir statut, Pageable pageable) {
        return devoirRepository.rechercher(recherche, statut, null, classeId, pageable)
                .map(mappers::toDto);
    }

    public Page<DevoirDto> afficher(Pageable pagination) {
        return devoirRepository.findAll(pagination).map(mappers::toDto);
    }

    @CacheEvict(value = "devoirs", allEntries = true)
    public DevoirDto creer(DevoirDto devoirDto) {
        Devoir devoir = mappers.toEntite(devoirDto);

        return mappers.toDto(devoirRepository.save(devoir));
    }

    @CacheEvict(value = "devoirs", allEntries = true)
    public DevoirDto modifier(Long id, DevoirDto devoirDto) {
        Devoir devoir = findById(id);
        mappers.update(devoirDto, devoir);

        return mappers.toDto(devoirRepository.save(devoir));
    }


    public void supprimer(Long id) {
        Devoir devoir = findById(id);
        devoirRepository.delete(devoir);

    }

    public Page<DevoirDto> chercherParEnseignant(Long enseignantId, StatutDevoir statut, Pageable pagination) {
        return devoirRepository.chercherParEnseignant(enseignantId, statut, pagination)
                .map(mappers::toDto);
    }

    private Devoir findById(Long id) {
        return devoirRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Devoir introuvable"));
    }
}
