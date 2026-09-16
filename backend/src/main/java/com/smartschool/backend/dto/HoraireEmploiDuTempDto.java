package com.smartschool.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HoraireEmploiDuTempDto {
    private Long id;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(max = 30, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String jour;

    @NotNull(message = "Ce champ est obligatoire.")
    private LocalTime heureDebut;

    @NotNull(message = "Ce champ est obligatoire.")
    private Long matiereId;

    @NotNull(message = "Ce champ est obligatoire.")
    private Long classeId;

    @NotNull(message = "Ce champ est obligatoire.")
    private Long enseignantId;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(max = 50, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String salle;

}
