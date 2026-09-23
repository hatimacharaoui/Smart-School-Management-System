package com.smartschool.backend.controller;

import com.smartschool.backend.dto.EleveDto;
import com.smartschool.backend.service.EleveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/eleves")
@RequiredArgsConstructor
public class EleveController {
    private final EleveService eleveService;

    @GetMapping
    public ResponseEntity<Page<EleveDto>> afficher(Pageable pagination) {

        return ResponseEntity.ok(eleveService.afficher(pagination));
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<Page<EleveDto>> chercherParParent(@PathVariable Long parentId, Pageable pageable) {

        return ResponseEntity.ok(eleveService.chercherParParent(parentId, pageable));
    }
}
