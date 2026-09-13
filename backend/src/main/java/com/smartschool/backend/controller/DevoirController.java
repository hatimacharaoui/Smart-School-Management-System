package com.smartschool.backend.controller;

import com.smartschool.backend.dto.DevoirDto;
import com.smartschool.backend.service.DevoirService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/devoirs")
@RequiredArgsConstructor
public class DevoirController {
    private final DevoirService devoirService;

    @GetMapping
    public ResponseEntity<Page<DevoirDto>> afficher(Pageable pagination) {
        return ResponseEntity.ok(devoirService.afficher(pagination));
    }
}
