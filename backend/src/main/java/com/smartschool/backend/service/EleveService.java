package com.smartschool.backend.service;

import com.smartschool.backend.dto.EleveDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EleveService {

    Page<EleveDto> chercherParParent(Long parentId, Pageable pageable);

    Page<EleveDto> afficher(String recherche, Pageable pagination);

    Page<EleveDto> chercherParClasse(Long classeId, String recherche, Pageable pagination);

    EleveDto chercherParId(Long id);

    EleveDto creer(EleveDto dto);

    EleveDto modifier(Long id, EleveDto dto);

    void Supprimer(Long id);


}
