package com.smartschool.backend.controller;

import com.smartschool.backend.dto.ClasseScolaireDto;
import com.smartschool.backend.service.ClasseScolaireService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClasseScolaireController {
    private final ClasseScolaireService classeScolaireService;


    @GetMapping
    public ResponseEntity<Page<ClasseScolaireDto>> afficherClasses(@RequestParam(required = false) String recherche, Pageable pagination) {

        return ResponseEntity.ok(classeScolaireService.afficherClasses(recherche, pagination));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClasseScolaireDto> chercher(@PathVariable Long id) {
        return ResponseEntity.ok(classeScolaireService.chercherParId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<ClasseScolaireDto> ajouter(
            @Valid @RequestBody ClasseScolaireDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(classeScolaireService.enregistrer(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<ClasseScolaireDto> modifier(
            @PathVariable Long id,
            @Valid @RequestBody ClasseScolaireDto dto
    ) {
        return ResponseEntity.ok(classeScolaireService.modifier(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        classeScolaireService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
