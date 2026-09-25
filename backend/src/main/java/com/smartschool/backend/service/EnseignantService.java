package com.smartschool.backend.service;

import com.smartschool.backend.dto.EnseignantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EnseignantService {

    Page<EnseignantDto> afficherEnseignants(String recherche, Pageable pageable);

    EnseignantDto chercherParId(Long id);

    EnseignantDto enregistrer(EnseignantDto dto);

    EnseignantDto modifier(Long id, EnseignantDto dto);

    void supprimer(Long id);
}
