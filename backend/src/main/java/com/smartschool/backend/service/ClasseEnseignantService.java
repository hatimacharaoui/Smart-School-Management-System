package com.smartschool.backend.service;

import com.smartschool.backend.dto.ClasseEnseignantDto;
import com.smartschool.backend.entity.ClasseEnseignant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClasseEnseignantService {

    Page<ClasseEnseignantDto> chercherParEnseignant(Long enseignantId, Pageable pageable);

}
