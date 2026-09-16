package com.smartschool.backend.dto;

import com.smartschool.backend.entity.StatutDevoir;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevoirDto {
    private Long id;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(max = 180, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String titre;

    @Size(max = 1000, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String description;

    @NotNull(message = "Ce champ est obligatoire.")
    private Long matiereId;

    @NotNull(message = "Ce champ est obligatoire.")
    private Long classeId;

    @NotNull(message = "Ce champ est obligatoire.")
    private Long enseignantId;

    @NotNull(message = "Ce champ est obligatoire.")
    private LocalDate dateLimite;

    @NotNull(message = "Ce champ est obligatoire.")
    private StatutDevoir statut;
}
