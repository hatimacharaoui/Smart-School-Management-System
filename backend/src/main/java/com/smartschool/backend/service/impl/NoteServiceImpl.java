package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.NoteDto;
import com.smartschool.backend.entity.Note;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.HoraireEmploiDuTempRepository;
import com.smartschool.backend.repository.NoteRepository;
import com.smartschool.backend.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final Mappers mappers;
    private final HoraireEmploiDuTempRepository horaireEmploiDuTempRepository;

    public Page<NoteDto> chercherParEleve(Long id, Pageable pageable) {
        return noteRepository.findByEleveId(id, pageable)
                .map(mappers::toDto);
    }

    public Page<NoteDto> afficher(Pageable pagination) {
        return noteRepository.findAll(pagination)
                .map(mappers::toDto);
    }

    public NoteDto creer(NoteDto dto) {
        Note note = mappers.toEntite(dto);

        return mappers.toDto(noteRepository.save(note));
    }

    public NoteDto modifier(Long id, NoteDto dto) {
        Note note = findById(id);
        mappers.update(dto, note);
        return mappers.toDto(noteRepository.save(note));
    }

    public void supprimer(Long id) {
        Note note = findById(id);
        noteRepository.delete(note);
    }

    private Note findById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note introuvable"));
    }

}
