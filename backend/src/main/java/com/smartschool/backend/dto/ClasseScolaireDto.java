package com.smartschool.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClasseScolaireDto {
    private Long id;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(max = 30, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String nom;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(max = 80, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String niveau;

    private Long enseignantPrincipalId;
}
