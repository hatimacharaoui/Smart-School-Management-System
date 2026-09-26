package com.smartschool.backend.controller;

import com.smartschool.backend.dto.MatiereDto;
import com.smartschool.backend.service.MatiereService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matieres")
public class MatiereController {
    private final MatiereService matiereService;


    @GetMapping
    public ResponseEntity<Page<MatiereDto>> afficherMatieres(
            @RequestParam(required = false) String recherche, Pageable pagination) {

        return ResponseEntity.ok(matiereService.afficherMatieres(recherche, pagination));
    }


    @GetMapping("/{id}")
    public ResponseEntity<MatiereDto> chercher(@PathVariable Long id) {
        return ResponseEntity.ok(matiereService.chercherParId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<MatiereDto> ajouter(@Valid @RequestBody MatiereDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(matiereService.enregistrer(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<MatiereDto> modifier(
            @PathVariable Long id,
            @Valid @RequestBody MatiereDto dto
    ) {
        return ResponseEntity.ok(matiereService.modifier(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        matiereService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
