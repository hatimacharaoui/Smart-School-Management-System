package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.EnseignantDto;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.EnseignantRepository;
import com.smartschool.backend.service.EnseignantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnseignantServiceImpl implements EnseignantService {
    private final EnseignantRepository enseignantRepository;
    private final Mappers mappers;


    public Page<EnseignantDto> afficherEnseignants(Pageable pageable) {
        return enseignantRepository.findAll(pageable)
                .map(mappers::toDto);
    }
}
