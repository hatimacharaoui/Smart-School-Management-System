package com.smartschool.backend.controller;


import com.smartschool.backend.dto.PaiementDto;
import com.smartschool.backend.service.PaiementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
public class PaiementController {
    private final PaiementService paiementService;


    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Page<PaiementDto>> afficher(Pageable pagination) {

        return ResponseEntity.ok(paiementService.afficher(pagination));
    }
}
