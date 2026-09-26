package com.smartschool.backend.service;

import com.smartschool.backend.dto.DevoirDto;
import com.smartschool.backend.entity.StatutDevoir;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DevoirService {

    Page<DevoirDto> chercherParClasse(Long id, String recherche, StatutDevoir statut, Pageable pageable);

    Page<DevoirDto> afficher(String recherche, StatutDevoir statut, Pageable pagination);

    DevoirDto chercherParId(Long id);

    DevoirDto creer(DevoirDto devoirDto);

    DevoirDto modifier(Long id, DevoirDto devoirDto);

    void supprimer(Long id);

    DevoirDto modifierStatut(Long id, StatutDevoir statut);

    Page<DevoirDto> chercherParEnseignant(Long enseignantId, String recherche, StatutDevoir statut, Pageable pagination);

}
