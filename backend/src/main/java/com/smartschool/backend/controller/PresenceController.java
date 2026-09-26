package com.smartschool.backend.controller;


import com.smartschool.backend.dto.PresenceDto;
import com.smartschool.backend.service.PresenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/presences")
@RequiredArgsConstructor
public class PresenceController {
    private final PresenceService presenceService;


    @GetMapping("/eleve/{id}")
    public ResponseEntity<Page<PresenceDto>> chercherParEleve(@PathVariable Long id, Pageable pageable) {
        return ResponseEntity.ok(presenceService.chercherParEleve(id, pageable));
    }

    @GetMapping("/absent-retard")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Page<PresenceDto>> afficherPresence(Pageable pagination) {
        return ResponseEntity.ok(presenceService.afficherPresence(pagination));
    }


    @GetMapping("/classe/{classeId}")
    public ResponseEntity<Page<PresenceDto>> chercherParClasseEtDate(
            @PathVariable Long classeId, @RequestParam LocalDate date, Pageable pagination) {

        return ResponseEntity.ok(
                presenceService.chercherParClasseEtDate(classeId, date, pagination));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR','ENSEIGNANT')")
    public ResponseEntity<PresenceDto> enregistrer(@Valid @RequestBody PresenceDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(presenceService.enregistrer(dto));
    }

    @PostMapping("/groupe")
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR','ENSEIGNANT')")
    public ResponseEntity<List<PresenceDto>> enregistrerTout(
            @Valid @RequestBody List<@Valid PresenceDto> dtos) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(presenceService.enregistrerTout(dtos));
    }



}
