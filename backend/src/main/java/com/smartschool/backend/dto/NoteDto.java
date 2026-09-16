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

    @NotNull(message = "Ce champ est obligatoire.")
    private Long eleveId;

    private Long devoirId;

    private Long enseignantId;

    @DecimalMin(value = "0", message = "La valeur doit être supérieure ou égale à {value}.")
    @DecimalMax(value = "20", message = "La valeur doit être inférieure ou égale à {value}.")
    private double valeur;

    private double valeurMaximal;

    private LocalDate date;

    @Size(max = 500, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String commentaire;
}
