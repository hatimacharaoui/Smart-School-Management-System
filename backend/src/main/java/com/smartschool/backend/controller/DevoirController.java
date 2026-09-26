package com.smartschool.backend.controller;

import com.smartschool.backend.dto.DevoirDto;
import com.smartschool.backend.entity.StatutDevoir;
import com.smartschool.backend.service.DevoirService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devoirs")
@RequiredArgsConstructor
public class DevoirController {
    private final DevoirService devoirService;

    @GetMapping("/classes/{id}")
    public ResponseEntity<Page<DevoirDto>> chercherParClasse(
            @PathVariable Long id, @RequestParam(required = false) String recherche,
            @RequestParam(required = false) StatutDevoir statut, Pageable pageable)
    {
        return ResponseEntity.ok(devoirService.chercherParClasse(id, recherche, statut, pageable));
    }

    @GetMapping
    public ResponseEntity<Page<DevoirDto>> afficher(@RequestParam(required = false) String recherche,
            @RequestParam(required = false) StatutDevoir statut, Pageable pagination) {

        return ResponseEntity.ok(devoirService.afficher(recherche, statut, pagination));
    }

    @GetMapping("/enseignant/{id}")
    public ResponseEntity<Page<DevoirDto>> chercherParEnseignant(
            @PathVariable Long id, @RequestParam(required = false) String recherche,
             @RequestParam(required = false)StatutDevoir statut, Pageable pagination) {
        return ResponseEntity.ok(devoirService.chercherParEnseignant(id, recherche, statut, pagination));
    }


    @GetMapping("/{id}")
    public ResponseEntity<DevoirDto> chercher(@PathVariable Long id) {
        return ResponseEntity.ok(devoirService.chercherParId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR','ENSEIGNANT')")
    public ResponseEntity<DevoirDto> modifier(
            @PathVariable Long id,
            @Valid @RequestBody DevoirDto dto
    ) {
        return ResponseEntity.ok(devoirService.modifier(id, dto));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR','ENSEIGNANT')")
    public ResponseEntity<DevoirDto> creer(@Valid @RequestBody DevoirDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(devoirService.creer(dto));
    }


    @PatchMapping("/{id}/statut")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public ResponseEntity<DevoirDto> modifierStatut(
            @PathVariable Long id,
            @RequestParam StatutDevoir statut
    ) {
        return ResponseEntity.ok(devoirService.modifierStatut(id, statut));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR','ENSEIGNANT')")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        devoirService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}

