package com.smartschool.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EleveDto {
    private Long id;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(max = 30, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String matricule;

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

    @Past(message = "La date doit être antérieure à aujourd'hui.")
    private LocalDate dateNaissance;

    @Size(max = 250, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String adresse;

    @NotNull(message = "Ce champ est obligatoire.")
    private Long classeId;

    @NotNull(message = "Ce champ est obligatoire.")
    private Long parentId;

    private boolean actif;
}
