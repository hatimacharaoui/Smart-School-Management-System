package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.ClasseScolaireDto;
import com.smartschool.backend.entity.ClasseScolaire;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.ClasseEnseignantRepository;
import com.smartschool.backend.repository.ClasseScolaireRepository;
import com.smartschool.backend.service.ClasseScolaireService;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClasseScolaireServiceImpl implements ClasseScolaireService {
    private final ClasseScolaireRepository classeScolaireRepository;
    private final Mappers mappers;

    public Page<ClasseScolaireDto> afficherClasses(String recherche, Pageable pagination) {
        if (recherche == null || recherche.isBlank()) {

        return classeScolaireRepository.findAll(pagination).map(mappers::toDto);
        }
        return classeScolaireRepository.findByNomContainingIgnoreCase(recherche, pagination)
                .map(mappers::toDto);
    }


    @Override
    @Cacheable(value = "classes", key = "'id-' + #id")
    public ClasseScolaireDto chercherParId(Long id) {
        return mappers.toDto(findById(id));
    }

    @Override
    @CacheEvict(value = "classes", allEntries = true)
    public ClasseScolaireDto enregistrer(ClasseScolaireDto dto) {
        ClasseScolaire classe = mappers.toEntite(dto);
        return mappers.toDto(classeScolaireRepository.save(classe));
    }

    @Override
    @CacheEvict(value = "classes", allEntries = true)
    public ClasseScolaireDto modifier(Long id, ClasseScolaireDto dto) {
        ClasseScolaire classe = findById(id);
        mappers.update(dto, classe);
        return mappers.toDto(classeScolaireRepository.save(classe));
    }

    @Override
    @CacheEvict(value = "classes", allEntries = true)
    public void supprimer(Long id) {
        ClasseScolaire classe = findById(id);

        classeScolaireRepository.delete(classe);
    }

    private ClasseScolaire findById(Long id) {
        return classeScolaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classe introuvable"));
    }
}
