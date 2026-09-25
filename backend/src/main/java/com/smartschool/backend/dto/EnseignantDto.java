package com.smartschool.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnseignantDto {
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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 5, max = 72, message = "Le mot de passe doit contenir entre 5 et 72 caractères")
    private String motDePasse;

    @Size(max = 30, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String telephone;

    @NotNull(message = "Ce champ est obligatoire.")
    private Long matiereId;

    private boolean actif;
}
