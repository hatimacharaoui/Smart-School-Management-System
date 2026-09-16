package com.smartschool.backend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentDto {
    private Long id;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(max = 100, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String prenom;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(max = 100, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String nom;

    @Email(message = "L'adresse email n'est pas valide.")
    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(max = 180, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String email;

    @Size(max = 30, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String telephone;

    private boolean actif;
}
