package com.smartschool.backend.controller;

import com.smartschool.backend.dto.NoteDto;
import com.smartschool.backend.dto.NotesGroupeesDto;
import com.smartschool.backend.service.NoteService;
import com.smartschool.backend.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;


    @GetMapping("/eleve/{id}")
    public ResponseEntity<Page<NoteDto>> chercherParEleve(@PathVariable Long id, Pageable pageable) {

        return ResponseEntity.ok(noteService.chercherParEleve(id, pageable));
    }


    @GetMapping("/devoir/{id}")
    public ResponseEntity<Page<NoteDto>> chercherParDevoir(
            @PathVariable Long id,
            Pageable pagination
    ) {
        return ResponseEntity.ok(noteService.chercherParDevoir(id, pagination));
    }

    @PostMapping("/groupe")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public ResponseEntity<List<NoteDto>> enregistrerTout(
            @Valid @RequestBody NotesGroupeesDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(noteService.enregistrerTout(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public ResponseEntity<NoteDto> modifier(
            @PathVariable Long id,
            @Valid @RequestBody NoteDto dto
    ) {
        return ResponseEntity.ok(noteService.modifier(id, dto));
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR','ENSEIGNANT','ELEVE','PARENT')")
    public ResponseEntity<Page<NoteDto>> lister(
            @RequestParam(required = false) Long eleveId,
            @RequestParam(required = false) Long devoirId,
            @RequestParam(required = false) Long enseignantId,
            @RequestParam(required = false) Long classeId,
            @RequestParam(required = false) Long matiereId,
            @RequestParam(required = false) String rechercheEleve,
            Pageable pagination
    ) {
        return ResponseEntity.ok(noteService.lister(
                eleveId,
                devoirId,
                enseignantId,
                classeId,
                matiereId,
                rechercheEleve,
                pagination
        ));
    }
}
