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

    @NotBlank
    @Size(max = 30)
    private String matricule;

    @NotBlank
    @Size(max = 100)
    private String prenom;

    @NotBlank
    @Size(max = 100)
    private String nom;

    @Email
    @NotBlank
    @Size(max = 180)
    private String email;

    @Size(max = 30)
    private String telephone;

    @Past
    private LocalDate dateNaissance;

    @Size(max = 250)
    private String adresse;

    @NotNull
    private Long classeId;

    @NotNull
    private Long parentId;
    private boolean actif;
}
