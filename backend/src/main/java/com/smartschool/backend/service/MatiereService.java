package com.smartschool.backend.service;

import com.smartschool.backend.dto.MatiereDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MatiereService {

    Page<MatiereDto> afficherMatieres(Pageable pagination);

}
