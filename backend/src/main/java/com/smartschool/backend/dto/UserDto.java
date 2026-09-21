package com.smartschool.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartschool.backend.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(max = 150, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String nomComplet;

    @Email(message = "L'adresse email n'est pas valide.")
    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(max = 180, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String email;

    private Role role;

    @Size(max = 30, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String telephone;

    private boolean actif;
}
