package com.smartschool.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthentificationDto {

    private Long id;
    private String nomComplet;

    @Email(message = "L'adresse email n'est pas valide.")
    @NotBlank(message = "Ce champ est obligatoire.")
    private String email;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(
            min = 6,
            max = 100,
            message = "La longueur doit être comprise entre 6 et 100 caractères."
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String motDePasse;

    private String token;
    private String telephone;
    private String role;
}
