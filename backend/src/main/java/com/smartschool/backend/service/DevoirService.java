package com.smartschool.backend.service;

import com.smartschool.backend.dto.DevoirDto;
import com.smartschool.backend.entity.StatutDevoir;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DevoirService {

    Page<DevoirDto> afficher(Pageable pagination);

    DevoirDto creer(DevoirDto devoirDto);

    DevoirDto modifier(Long id, DevoirDto devoirDto);

    void supprimer(Long id);

    Page<DevoirDto> chercherParEnseignant(Long enseignantId, StatutDevoir statut, Pageable pagination);

}
