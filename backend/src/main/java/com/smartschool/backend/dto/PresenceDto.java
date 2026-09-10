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

    @NotNull
    private Long eleveId;

    @NotNull
    private Long classeId;

    @NotNull
    private Long matiereId;

    @NotNull
    private Long enseignantId;

    @NotNull
    private LocalDate date;

    @NotNull
    private StatutPresence statut;
}
