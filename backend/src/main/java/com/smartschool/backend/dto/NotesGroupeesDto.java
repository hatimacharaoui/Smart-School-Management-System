package com.smartschool.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotesGroupeesDto {
        @NotNull(message = "Ce champ est obligatoire.")
        private Long devoirId;

        @NotNull(message = "Ce champ est obligatoire.")
        private Long enseignantId;

        @NotEmpty(message = "Cette liste ne doit pas être vide.")
        private List<@Valid NoteDto> notes;
}
