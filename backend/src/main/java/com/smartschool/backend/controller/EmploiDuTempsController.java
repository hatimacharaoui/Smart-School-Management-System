package com.smartschool.backend.controller;

import com.smartschool.backend.dto.HoraireEmploiDuTempDto;
import com.smartschool.backend.service.EmploiDuTempsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emploi-du-temps")
@RequiredArgsConstructor
public class EmploiDuTempsController {
    private final EmploiDuTempsService emploiDuTempsService;


    @GetMapping
    public ResponseEntity<Page<HoraireEmploiDuTempDto>> afficher(
            @RequestParam(required = false) String jour,
            @RequestParam(required = false) Long classeId,
            @RequestParam(required = false) Long enseignantId, Pageable pageable) {

        return ResponseEntity.ok(emploiDuTempsService.afficher(jour, classeId, enseignantId, pageable));
    }

}
