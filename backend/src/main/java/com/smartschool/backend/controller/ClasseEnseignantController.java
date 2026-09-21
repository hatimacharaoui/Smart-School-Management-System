package com.smartschool.backend.controller;

import com.smartschool.backend.dto.ClasseEnseignantDto;
import com.smartschool.backend.entity.ClasseEnseignant;
import com.smartschool.backend.service.ClasseEnseignantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ClasseEnseignantController {
    private final ClasseEnseignantService classeEnseignantService;


    @GetMapping("/enseignants/{enseignantId}/classes")
    public ResponseEntity<Page<ClasseEnseignantDto>> chercherParEnseignant(@PathVariable Long enseignantId, Pageable pageable) {
        return ResponseEntity.ok(classeEnseignantService.chercherParEnseignant(enseignantId, pageable));
    }




}
