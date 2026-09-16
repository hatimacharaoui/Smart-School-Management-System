package com.smartschool.backend.dto;

import com.smartschool.backend.entity.StatutPresence;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PresenceDto {
    private Long id;

    @NotNull(message = "Ce champ est obligatoire.")
    private Long eleveId;

    @NotNull(message = "Ce champ est obligatoire.")
    private Long classeId;

    @NotNull(message = "Ce champ est obligatoire.")
    private Long matiereId;

    @NotNull(message = "Ce champ est obligatoire.")
    private Long enseignantId;

    @NotNull(message = "Ce champ est obligatoire.")
    private LocalDate date;

    @NotNull(message = "Ce champ est obligatoire.")
    private StatutPresence statut;
}
