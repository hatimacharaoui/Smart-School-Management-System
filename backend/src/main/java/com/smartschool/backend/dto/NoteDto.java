package com.smartschool.backend.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteDto {
    private Long id;

    @NotNull
    private Long eleveId;

    private Long devoirId;
    private Long enseignantId;

    @DecimalMin("0") @DecimalMax("20")
    private double valeur;

    private double valeurMaximal;
    private LocalDate date;

    @Size(max = 500)
    private String commentaire;
}
