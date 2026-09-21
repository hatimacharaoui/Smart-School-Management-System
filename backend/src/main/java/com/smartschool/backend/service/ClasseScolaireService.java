package com.smartschool.backend.service;

import com.smartschool.backend.dto.ClasseScolaireDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClasseScolaireService {

    Page<ClasseScolaireDto> afficherClasses(Pageable pagination);

}
