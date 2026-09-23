package com.smartschool.backend.controller;

import com.smartschool.backend.dto.NoteDto;
import com.smartschool.backend.service.NoteService;
import com.smartschool.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;


    @GetMapping("/eleve/{id}")
    public ResponseEntity<Page<NoteDto>> chercherParEleve(@PathVariable Long id, Pageable pageable) {

        return ResponseEntity.ok(noteService.chercherParEleve(id, pageable));
    }
}
