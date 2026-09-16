package com.smartschool.backend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClasseEnseignantDto {
    private Long id;

    @NotNull(message = "Ce champ est obligatoire.")
    private Long enseignantId;

    @NotNull(message = "Ce champ est obligatoire.")
    private Long classeId;
}
