package com.smartschool.backend.service;

import com.smartschool.backend.dto.EleveDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EleveService {

    Page<EleveDto> chercherParParent(Long parentId, Pageable pageable);

    Page<EleveDto> afficher(Pageable pagination);

    EleveDto creer(EleveDto dto);

    EleveDto modifier(Long id, EleveDto dto);

    void Supprimer(Long id);


}
