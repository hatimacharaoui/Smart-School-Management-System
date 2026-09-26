package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.ClasseScolaireDto;
import com.smartschool.backend.dto.MatiereDto;
import com.smartschool.backend.entity.Matiere;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.MatiereRepository;
import com.smartschool.backend.service.MatiereService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatiereServiceImpl implements MatiereService {
    private final MatiereRepository matiereRepository;
    private final Mappers mappers;

    public Page<MatiereDto> afficherMatieres(String recherche, Pageable pagination) {
        if (recherche == null || recherche.isBlank()) {

            return matiereRepository.findAll(pagination).map(mappers::toDto);
        }
        return matiereRepository.findByNomContainingIgnoreCase(recherche, pagination)
                .map(mappers::toDto);
    }


    @Cacheable(value = "matieres", key = "'id-' + #id")
    public MatiereDto chercherParId(Long id) {
        return mappers.toDto(findById(id));
    }


    @CacheEvict(value = "matieres", allEntries = true)
    public MatiereDto enregistrer(MatiereDto dto) {
        Matiere matiere = mappers.toEntite(dto);
        return mappers.toDto(matiereRepository.save(matiere));
    }


    @CacheEvict(value = "matieres", allEntries = true)
    public MatiereDto modifier(Long id, MatiereDto dto) {
        Matiere matiere = findById(id);
        mappers.update(dto, matiere);
        return mappers.toDto(matiereRepository.save(matiere));
    }


    @CacheEvict(value = "matieres", allEntries = true)
    public void supprimer(Long id) {
        Matiere matiere = findById(id);

        matiereRepository.delete(matiere);
    }

    private Matiere findById(Long id) {
        return matiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matière introuvable"));
    }

}
