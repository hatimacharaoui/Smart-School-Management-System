package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.ClasseScolaireDto;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.ClasseEnseignantRepository;
import com.smartschool.backend.repository.ClasseScolaireRepository;
import com.smartschool.backend.service.ClasseScolaireService;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClasseScolaireServiceImpl implements ClasseScolaireService {
    private final ClasseScolaireRepository classeScolaireRepository;
    private final Mappers mappers;

    public Page<ClasseScolaireDto> afficherClasses(Pageable pagination) {
        return classeScolaireRepository.findAll(pagination).map(mappers::toDto);
    }
}
