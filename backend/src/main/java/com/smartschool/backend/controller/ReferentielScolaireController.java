package com.smartschool.backend.controller;

import com.smartschool.backend.dto.*;
import com.smartschool.backend.service.ReferentielScolaireService;
import com.smartschool.backend.service.impl.ReferentielScolaireServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReferentielScolaireController {
    private final ReferentielScolaireService referentielScolaireService;


    @GetMapping("/enseignants")
    public ResponseEntity<Page<EnseignantDto>> afficherEnseignants(Pageable pagination) {

        return ResponseEntity.ok(referentielScolaireService.afficherEnseignants(pagination));
    }


    @GetMapping("/parents")
    public ResponseEntity<Page<ParentDto>> afficherParents(Pageable pagination) {
        return ResponseEntity.ok(referentielScolaireService.afficherParents(pagination));
    }


    @GetMapping("/classes")
    public ResponseEntity<Page<ClasseScolaireDto>> afficherClasses(Pageable pagination) {
        return ResponseEntity.ok(referentielScolaireService.afficherClasses(pagination));
    }


    @GetMapping("/matieres")
    public ResponseEntity<Page<MatiereDto>> afficherMatieres(Pageable pagination) {
        return ResponseEntity.ok(referentielScolaireService.afficherMatieres(pagination));
    }


    @GetMapping("/affectations-classes")
    public ResponseEntity<Page<ClasseEnseignantDto>> afficherClasseEnseignant(Pageable pagination) {
        return ResponseEntity.ok(referentielScolaireService.afficherAffectionsClasses(pagination));
    }
}
