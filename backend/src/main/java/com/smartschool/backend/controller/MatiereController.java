package com.smartschool.backend.controller;

import com.smartschool.backend.dto.MatiereDto;
import com.smartschool.backend.service.MatiereService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matieres")
public class MatiereController {
    private final MatiereService matiereService;


    @GetMapping
    public ResponseEntity<Page<MatiereDto>> afficherMatieres(Pageable pagination) {

        return ResponseEntity.ok(matiereService.afficherMatieres(pagination));
    }
}
