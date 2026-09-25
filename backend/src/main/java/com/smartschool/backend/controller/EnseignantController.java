package com.smartschool.backend.controller;

import com.smartschool.backend.dto.EnseignantDto;
import com.smartschool.backend.service.EnseignantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enseignants")
@RequiredArgsConstructor
public class EnseignantController {
    private final EnseignantService enseignantService;

    @GetMapping
    public ResponseEntity<Page<EnseignantDto>> afficherEnseignants(@RequestParam(required = false) String recherche, Pageable pagination) {

        return ResponseEntity.ok(enseignantService.afficherEnseignants(recherche, pagination));
    }


    @GetMapping("/{id}")
    public ResponseEntity<EnseignantDto> chercher(@PathVariable Long id) {
        return ResponseEntity.ok(enseignantService.chercherParId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<EnseignantDto> ajouter(@Valid @RequestBody EnseignantDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enseignantService.enregistrer(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR','ENSEIGNANT')")
    public ResponseEntity<EnseignantDto> modifier(
            @PathVariable Long id,
            @Valid @RequestBody EnseignantDto dto
    ) {
        return ResponseEntity.ok(enseignantService.modifier(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        enseignantService.supprimer(id);
        return ResponseEntity.noContent().build();
    }


}
