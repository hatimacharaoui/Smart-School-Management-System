package com.smartschool.backend.controller;

import com.smartschool.backend.dto.HoraireEmploiDuTempDto;
import com.smartschool.backend.service.EmploiDuTempsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emploi-du-temps")
@RequiredArgsConstructor
public class EmploiDuTempsController {
    private final EmploiDuTempsService emploiDuTempsService;


    @GetMapping
    public ResponseEntity<Page<HoraireEmploiDuTempDto>> afficher(
            @RequestParam(required = false) String jour,
            @RequestParam(required = false) Long classeId,
            @RequestParam(required = false) Long enseignantId, Pageable pageable) {

        return ResponseEntity.ok(emploiDuTempsService.afficher(jour, classeId, enseignantId, pageable));
    }


    @GetMapping("/jour/{jour}")
    public ResponseEntity<Page<HoraireEmploiDuTempDto>> chercherParJour(
            @PathVariable String jour,
            Pageable pagination
    ) {
        return ResponseEntity.ok(emploiDuTempsService.chercherParJour(jour, pagination));
    }

    @GetMapping("/enseignant/{id}")
    public ResponseEntity<Page<HoraireEmploiDuTempDto>> chercherParEnseignant(
            @PathVariable Long id,
            Pageable pagination
    ) {
        return ResponseEntity.ok(
                emploiDuTempsService.chercherParEnseignant(id, pagination)
        );
    }

    @GetMapping("/classe/{id}")
    public ResponseEntity<Page<HoraireEmploiDuTempDto>> chercherParClasse(
            @PathVariable Long id,
            Pageable pagination
    ) {
        return ResponseEntity.ok(emploiDuTempsService.chercherParClasse(id, pagination));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<HoraireEmploiDuTempDto> enregistrer(
            @Valid @RequestBody HoraireEmploiDuTempDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(emploiDuTempsService.enregistrer(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<HoraireEmploiDuTempDto> modifier(
            @PathVariable Long id,
            @Valid @RequestBody HoraireEmploiDuTempDto dto
    ) {
        return ResponseEntity.ok(emploiDuTempsService.modifier(id, dto));
    }
}
