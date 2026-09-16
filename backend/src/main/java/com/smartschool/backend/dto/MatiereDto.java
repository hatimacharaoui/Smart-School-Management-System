package com.smartschool.backend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatiereDto {
    private Long id;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(max = 100, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String nom;

    @Min(value = 1, message = "La valeur doit être supérieure ou égale à {value}.")
    @Max(value = 10, message = "La valeur doit être inférieure ou égale à {value}.")
    private int coefficient;

    private boolean actif;
}
