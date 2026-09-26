package com.smartschool.backend.controller;


import com.smartschool.backend.dto.PaiementDto;
import com.smartschool.backend.entity.StatutPaiement;
import com.smartschool.backend.service.JustificatifPaiementService;
import com.smartschool.backend.service.PaiementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
public class PaiementController {
    private final PaiementService paiementService;
    private final JustificatifPaiementService justificatifPaiementService;


    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Page<PaiementDto>> afficher(
            @RequestParam(required = false) StatutPaiement statut,
            @RequestParam(required = false) LocalDate dateDebut,
            @RequestParam(required = false) LocalDate dateFin, Pageable pagination) {

        return ResponseEntity.ok(paiementService.afficher(statut, dateDebut, dateFin, pagination));
    }

    @GetMapping("/eleve/{id}")
    public ResponseEntity<Page<PaiementDto>> chercherParEleve(
            @PathVariable Long id, @RequestParam(required = false)StatutPaiement statut,
            @RequestParam(required = false)LocalDate dateDebut, @RequestParam(required = false) LocalDate dateFin,
            Pageable pageable ) {

        return ResponseEntity.ok(paiementService.chercherParEleve(id, statut, dateDebut, dateFin, pageable));
    }
}
