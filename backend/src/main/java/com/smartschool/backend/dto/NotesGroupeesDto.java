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
        @NotNull
        private Long devoirId;

        @NotNull
        private Long enseignantId;

        @NotEmpty
        private List<@Valid NoteDto> notes;
}
