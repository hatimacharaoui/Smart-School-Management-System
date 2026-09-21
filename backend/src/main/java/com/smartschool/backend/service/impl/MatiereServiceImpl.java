package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.ClasseScolaireDto;
import com.smartschool.backend.dto.MatiereDto;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.MatiereRepository;
import com.smartschool.backend.service.MatiereService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatiereServiceImpl implements MatiereService {
    private final MatiereRepository matiereRepository;
    private final Mappers mappers;

    public Page<MatiereDto> afficherMatieres(Pageable pagination) {
        return matiereRepository.findAll(pagination).map(mappers::toDto);
    }
}
