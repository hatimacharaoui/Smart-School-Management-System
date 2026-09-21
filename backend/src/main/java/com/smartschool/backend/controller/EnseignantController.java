package com.smartschool.backend.controller;

import com.smartschool.backend.dto.EnseignantDto;
import com.smartschool.backend.service.EnseignantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enseignants")
@RequiredArgsConstructor
public class EnseignantController {
    private final EnseignantService enseignantService;

    @GetMapping
    public ResponseEntity<Page<EnseignantDto>> afficherEnseignants(Pageable pagination) {

        return ResponseEntity.ok(enseignantService.afficherEnseignants(pagination));
    }



}
