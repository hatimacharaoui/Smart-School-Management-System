package com.smartschool.backend.service;

import com.smartschool.backend.dto.PaiementDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaiementService {

    Page<PaiementDto> afficher(Pageable pagination);

    PaiementDto creer(PaiementDto dto);

    PaiementDto modifier(Long id, PaiementDto dto);

    void supprimer(Long id);
}
