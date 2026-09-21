package com.smartschool.backend.service;

import com.smartschool.backend.dto.EnseignantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EnseignantService {

    Page<EnseignantDto> afficherEnseignants(Pageable pageable);
}
