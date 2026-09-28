package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.AffectationClassesEnseignantDto;
import com.smartschool.backend.dto.ClasseEnseignantDto;
import com.smartschool.backend.entity.ClasseEnseignant;
import com.smartschool.backend.exception.ResourceNotFoundException;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.ClasseEnseignantRepository;
import com.smartschool.backend.repository.ClasseScolaireRepository;
import com.smartschool.backend.repository.EnseignantRepository;
import com.smartschool.backend.service.ClasseEnseignantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClasseEnseignantServiceImpl implements ClasseEnseignantService {
    private final ClasseEnseignantRepository classeEnseignantRepository;
    private final Mappers mappers;
    private final ClasseScolaireRepository classeScolaireRepository;
    private final EnseignantRepository enseignantRepository;

    @Override
    public Page<ClasseEnseignantDto> lister(Pageable pagination) {
        return classeEnseignantRepository.findAll(pagination).map(mappers::toDto);
    }

    @Override
    public Page<ClasseEnseignantDto> chercherParEnseignant(Long enseignantId, Pageable pagination) {

        return classeEnseignantRepository.findByEnseignantId(enseignantId, pagination)
                .map(mappers::toDto);
    }

    @Transactional
    public List<ClasseEnseignantDto> remplacerAffectations(Long enseignantId, AffectationClassesEnseignantDto affectation) {
        if (!enseignantRepository.existsById(enseignantId)) {
            throw new ResourceNotFoundException("Enseignant introuvable");
        }

        List<Long> classeIds = affectation.getClasseIds().stream()
                .distinct()
                .toList();

        for (Long classeId : classeIds) {
            if (!classeScolaireRepository.existsById(classeId)) {
                throw new ResourceNotFoundException("Classe introuvable : " + classeId);
            }
        }

        classeEnseignantRepository.deleteByEnseignantId(enseignantId);
        classeEnseignantRepository.flush();

        List<ClasseEnseignant> affectations = classeIds.stream()
                .map(classeId -> ClasseEnseignant.builder()
                        .enseignantId(enseignantId)
                        .classeId(classeId)
                        .build())
                .toList();

        return classeEnseignantRepository.saveAll(affectations).stream()
                .map(mappers::toDto)
                .toList();
    }
}
