package com.smartschool.backend.controller;

import com.smartschool.backend.dto.AffectationClassesEnseignantDto;
import com.smartschool.backend.dto.ClasseEnseignantDto;
import com.smartschool.backend.entity.ClasseEnseignant;
import com.smartschool.backend.service.ClasseEnseignantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ClasseEnseignantController {
    private final ClasseEnseignantService classeEnseignantService;


    @GetMapping("/enseignants/{enseignantId}/classes")
    public ResponseEntity<Page<ClasseEnseignantDto>> chercherParEnseignant(@PathVariable Long enseignantId, Pageable pageable) {
        return ResponseEntity.ok(classeEnseignantService.chercherParEnseignant(enseignantId, pageable));
    }

    @GetMapping("/affectations-classes")
    public ResponseEntity<Page<ClasseEnseignantDto>> lister(Pageable pagination) {
        return ResponseEntity.ok(classeEnseignantService.lister(pagination));
    }


    @PutMapping("/enseignants/{enseignantId}/classes")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<List<ClasseEnseignantDto>> remplacerAffectations(
            @PathVariable Long enseignantId,
            @Valid @RequestBody AffectationClassesEnseignantDto affectation
    ) {
        return ResponseEntity.ok(
                classeEnseignantService.remplacerAffectations(enseignantId, affectation));
    }


}
