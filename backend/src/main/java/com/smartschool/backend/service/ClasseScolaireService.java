package com.smartschool.backend.service;

import com.smartschool.backend.dto.ClasseScolaireDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClasseScolaireService {

    Page<ClasseScolaireDto> afficherClasses(String recherche, Pageable pagination);

    ClasseScolaireDto chercherParId(Long id);

    ClasseScolaireDto enregistrer(ClasseScolaireDto dto);

    ClasseScolaireDto modifier(Long id, ClasseScolaireDto dto);

    void supprimer(Long id);

}
