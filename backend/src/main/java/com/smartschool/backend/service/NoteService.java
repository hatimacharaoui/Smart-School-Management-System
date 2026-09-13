package com.smartschool.backend.service;

import com.smartschool.backend.dto.NoteDto;
import com.smartschool.backend.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteService {

    Page<NoteDto> afficher(Pageable pagination);

    NoteDto creer(NoteDto dto);

    NoteDto modifier(Long id, NoteDto dto);

    void supprimer(Long id);
}
