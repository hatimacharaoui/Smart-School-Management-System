package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.ClasseEnseignantDto;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.ClasseEnseignantRepository;
import com.smartschool.backend.service.ClasseEnseignantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClasseEnseignantServiceImpl implements ClasseEnseignantService {
    private final ClasseEnseignantRepository classeEnseignantRepository;
    private final Mappers mappers;


    public Page<ClasseEnseignantDto> chercherParEnseignant(Long enseignantId, Pageable pageable) {
        return classeEnseignantRepository.findByEnseignantId(enseignantId, pageable)
                .map(mappers::toDto);
    }
}
