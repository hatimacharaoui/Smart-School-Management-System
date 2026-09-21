package com.smartschool.backend.controller;

import com.smartschool.backend.dto.DevoirDto;
import com.smartschool.backend.entity.StatutDevoir;
import com.smartschool.backend.service.DevoirService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devoirs")
@RequiredArgsConstructor
public class DevoirController {
    private final DevoirService devoirService;

    @GetMapping
    public ResponseEntity<Page<DevoirDto>> afficher(Pageable pagination) {
        return ResponseEntity.ok(devoirService.afficher(pagination));
    }

    @GetMapping("/enseignant/{id}")
    public ResponseEntity<Page<DevoirDto>> chercherParEnseignant(
            @PathVariable Long id, @RequestParam(required = false)StatutDevoir statut, Pageable pagination) {
        return ResponseEntity.ok(devoirService.chercherParEnseignant(id, statut, pagination));
    }
}
