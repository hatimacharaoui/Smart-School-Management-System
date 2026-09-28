package com.smartschool.backend.service;

import com.smartschool.backend.dto.NoteDto;
import com.smartschool.backend.dto.NotesGroupeesDto;
import com.smartschool.backend.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoteService {

    Page<NoteDto> chercherParEleve(Long id, Pageable pageable);

    Page<NoteDto> lister(Long eleveId, Long devoirId, Long enseignantId,
                         Long classeId, Long matiereId, String rechercheEleve, Pageable pagination);

    Page<NoteDto> chercherParDevoir(Long devoirId, Pageable pagination);

    List<NoteDto> enregistrerTout(NotesGroupeesDto donnees);

    NoteDto modifier(Long id, NoteDto note);
}
