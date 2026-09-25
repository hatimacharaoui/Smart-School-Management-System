package com.smartschool.backend.service;

import com.smartschool.backend.dto.ParentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ParentService {
    Page<ParentDto> lister(String recherche, Pageable pagination);
    ParentDto chercherParId(Long id);
    ParentDto enregistrer(ParentDto parent);
    ParentDto modifier(Long id, ParentDto parent);
    void supprimer(Long id);

}
