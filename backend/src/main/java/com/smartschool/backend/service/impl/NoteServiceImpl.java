package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.NoteDto;
import com.smartschool.backend.dto.NotesGroupeesDto;
import com.smartschool.backend.entity.*;
import com.smartschool.backend.exception.ResourceNotFoundException;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.mapper.MappersImpl;
import com.smartschool.backend.repository.DevoirRepository;
import com.smartschool.backend.repository.HoraireEmploiDuTempRepository;
import com.smartschool.backend.repository.NoteRepository;
import com.smartschool.backend.service.NoteService;
import com.smartschool.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final DevoirRepository devoirRepository;
    private final NotificationService notificationService;
    private final Mappers mappers;

    public Page<NoteDto> lister(
            Long eleveId,
            Long devoirId,
            Long enseignantId,
            Long classeId,
            Long matiereId,
            String rechercheEleve,
            Pageable pagination
    ) {
        return noteRepository.rechercher(
                eleveId,
                devoirId,
                enseignantId,
                classeId,
                matiereId,
                rechercheEleve,
                pagination
        ).map(mappers::toDto);
    }

    public Page<NoteDto> chercherParEleve(Long eleveId, Pageable pagination) {
        return noteRepository.rechercher(
                eleveId,
                null,
                null,
                null,
                null,
                null,
                pagination
        ).map(mappers::toDto);
    }

    public Page<NoteDto> chercherParDevoir(Long devoirId, Pageable pagination) {
        return noteRepository.rechercher(
                null,
                devoirId,
                null,
                null,
                null,
                null,
                pagination
        ).map(mappers::toDto);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "notes", allEntries = true),
            @CacheEvict(value = "devoirs", allEntries = true)
    })
    public List<NoteDto> enregistrerTout(NotesGroupeesDto donnees) {
        Devoir devoir = devoirRepository.findById(donnees.getDevoirId())
                .orElseThrow(() -> new ResourceNotFoundException("Devoir introuvable"));

        for (NoteDto ligne : donnees.getNotes()) {
            if (ligne.getValeur() < 0 || ligne.getValeur() > 20) {
                throw new IllegalArgumentException("La note doit être comprise entre 0 et 20");
            }

            Optional<Note> noteExistante = noteRepository.findByEleveIdAndDevoirId(
                    ligne.getEleveId(),
                    devoir.getId()
            );
            Note note;
            if (noteExistante.isPresent()) {
                note = noteExistante.get();
                note.setValeur(ligne.getValeur());
                note.setCommentaire(ligne.getCommentaire());
                note.setDate(LocalDate.now());
            } else {
                note = Note.builder()
                        .eleveId(ligne.getEleveId())
                        .devoirId(devoir.getId())
                        .enseignantId(donnees.getEnseignantId())
                        .valeur(ligne.getValeur())
                        .valeurMaximale(20)
                        .date(LocalDate.now())
                        .commentaire(ligne.getCommentaire())
                        .build();
            }
            noteRepository.save(note);
        }

        devoir.setStatut(StatutDevoir.CORRIGE);
        devoirRepository.save(devoir);
        notificationService.notifierRole(
                Role.ELEVE,
                "Nouvelle note",
                "Une nouvelle note est disponible.",
                TypeNotification.NOTE,
                devoir.getId()
        );
        notificationService.notifierRole(
                Role.PARENT,
                "Nouvelle note",
                "Une nouvelle note est disponible pour votre enfant.",
                TypeNotification.NOTE,
                devoir.getId()
        );
        return noteRepository.rechercher(
                null,
                devoir.getId(),
                null,
                null,
                null,
                null,
                Pageable.unpaged()
        ).getContent().stream().map(mappers::toDto).toList();
    }

    @CacheEvict(value = "notes", allEntries = true)
    public NoteDto modifier(Long id, NoteDto donnees) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note introuvable"));
        if (donnees.getValeur() < 0 || donnees.getValeur() > 20) {
            throw new IllegalArgumentException("La note doit être comprise entre 0 et 20");
        }
        mappers.update(donnees, note);
        note.setDate(LocalDate.now());
        return mappers.toDto(noteRepository.save(note));
    }

    private Note findById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note introuvable"));
    }

}
