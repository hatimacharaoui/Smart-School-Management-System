package com.smartschool.backend.service;

import com.smartschool.backend.dto.PaiementDto;
import com.smartschool.backend.entity.StatutPaiement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PaiementService {

    Page<PaiementDto> chercherParEleve(Long eleveId, StatutPaiement statut,  LocalDate dateDebut, LocalDate dateFin, Pageable pagination);

    Page<PaiementDto> afficher(Pageable pagination);

    PaiementDto creer(PaiementDto dto);

    PaiementDto modifier(Long id, PaiementDto dto);

    void supprimer(Long id);
}
