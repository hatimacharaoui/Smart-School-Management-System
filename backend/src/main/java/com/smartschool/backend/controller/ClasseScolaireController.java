package com.smartschool.backend.controller;

import com.smartschool.backend.dto.ClasseScolaireDto;
import com.smartschool.backend.service.ClasseScolaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClasseScolaireController {
    private final ClasseScolaireService classeScolaireService;


    @GetMapping
    public ResponseEntity<Page<ClasseScolaireDto>> afficherClasses(Pageable pagination) {

        return ResponseEntity.ok(classeScolaireService.afficherClasses(pagination));
    }
}
