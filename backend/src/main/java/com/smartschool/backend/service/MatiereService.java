package com.smartschool.backend.service;

import com.smartschool.backend.dto.MatiereDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MatiereService {

    Page<MatiereDto> afficherMatieres(String recherche, Pageable pagination);

    MatiereDto chercherParId(Long id);

    MatiereDto enregistrer(MatiereDto matiere);

    MatiereDto modifier(Long id, MatiereDto matiere);

    void supprimer(Long id);
}
