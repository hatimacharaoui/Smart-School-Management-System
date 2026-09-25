package com.smartschool.backend.controller;

import com.smartschool.backend.dto.EleveDto;
import com.smartschool.backend.service.EleveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eleves")
@RequiredArgsConstructor
public class EleveController {
    private final EleveService eleveService;

    @GetMapping
    public ResponseEntity<Page<EleveDto>> afficher( @RequestParam(required = false) String recherche, Pageable pagination) {

        return ResponseEntity.ok(eleveService.afficher(recherche, pagination));
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<Page<EleveDto>> chercherParParent(@PathVariable Long parentId, Pageable pageable) {

        return ResponseEntity.ok(eleveService.chercherParParent(parentId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EleveDto> chercher(@PathVariable Long id) {
        return ResponseEntity.ok(eleveService.chercherParId(id));
    }

    @GetMapping("/classe/{classeId}")
    public ResponseEntity<Page<EleveDto>> chercherParClasse(
            @PathVariable Long classeId,
            @RequestParam(required = false) String recherche,
            Pageable pagination
    ) {
        return ResponseEntity.ok(
                eleveService.chercherParClasse(classeId, recherche, pagination)
        );
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<EleveDto> creer(@Valid @RequestBody EleveDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eleveService.creer(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<EleveDto> modifier(
            @PathVariable Long id,
            @Valid @RequestBody EleveDto dto
    ) {
        return ResponseEntity.ok(eleveService.modifier(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        eleveService.Supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
