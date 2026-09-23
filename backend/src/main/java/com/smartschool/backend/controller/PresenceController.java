package com.smartschool.backend.controller;


import com.smartschool.backend.dto.PresenceDto;
import com.smartschool.backend.service.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/presences")
@RequiredArgsConstructor
public class PresenceController {
    private final PresenceService presenceService;


    @GetMapping("/eleve/{id}")
    public ResponseEntity<Page<PresenceDto>> chercherParEleve(@PathVariable Long id, Pageable pageable) {

        return ResponseEntity.ok(presenceService.chercherParEleve(id, pageable));
    }

    @GetMapping("/absent-retard")
    public ResponseEntity<Page<PresenceDto>> afficherPresence(Pageable pagination) {

        return ResponseEntity.ok(presenceService.afficherPresence(pagination));
    }



}
