package com.smartschool.backend.controller;


import com.smartschool.backend.dto.PaiementDto;
import com.smartschool.backend.entity.StatutPaiement;
import com.smartschool.backend.service.JustificatifPaiementService;
import com.smartschool.backend.service.PaiementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR','PARENT')")
    public ResponseEntity<PaiementDto> chercher(@PathVariable Long id) {
        return ResponseEntity.ok(paiementService.chercherParId(id));
    }

    @GetMapping("/mensuels")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Page<PaiementDto>> listerMensuels(
            @RequestParam int annee,
            @RequestParam int mois,
            @RequestParam(required = false) StatutPaiement statut,
            Pageable pagination
    ) {
        return ResponseEntity.ok(
                paiementService.listerMensuels(annee, mois, statut, pagination)
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR','PARENT')")
    public ResponseEntity<PaiementDto> creer(@Valid @RequestBody PaiementDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paiementService.creer(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<PaiementDto> modifier(
            @PathVariable Long id,
            @Valid @RequestBody PaiementDto dto
    ) {
        return ResponseEntity.ok(paiementService.modifier(id, dto));
    }

    @PatchMapping("/{id}/statut")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<PaiementDto> modifierStatut(
            @PathVariable Long id,
            @RequestParam StatutPaiement statut
    ) {
        return ResponseEntity.ok(paiementService.modifierStatut(id, statut));
    }

    @PostMapping(value = "/{id}/justificatif", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<PaiementDto> ajouterJustificatif(
            @PathVariable Long id,
            @RequestParam("fichier") MultipartFile fichier
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(justificatifPaiementService.enregistrer(id, fichier));
    }

    @GetMapping("/{id}/justificatif")
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR','PARENT')")
    public ResponseEntity<Resource> voirJustificatif(@PathVariable Long id) {
        Resource fichier = justificatifPaiementService.charger(id);
        String typeContenu = justificatifPaiementService.determinerTypeContenu(fichier);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(typeContenu))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + fichier.getFilename() + "\""
                )
                .body(fichier);
    }
}
