package com.smartschool.backend.controller;

import com.smartschool.backend.dto.ParentDto;
import com.smartschool.backend.service.ParentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parents")
@RequiredArgsConstructor
public class ParentController {
    private final ParentService parentService;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Page<ParentDto>> lister(
            @RequestParam(required = false) String recherche,
            Pageable pagination
    ) {
        return ResponseEntity.ok(parentService.lister(recherche, pagination));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParentDto> chercher(@PathVariable Long id) {
        return ResponseEntity.ok(parentService.chercherParId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<ParentDto> ajouter(@Valid @RequestBody ParentDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(parentService.enregistrer(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR','PARENT')")
    public ResponseEntity<ParentDto> modifier(
            @PathVariable Long id,
            @Valid @RequestBody ParentDto dto
    ) {
        return ResponseEntity.ok(parentService.modifier(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        parentService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}

