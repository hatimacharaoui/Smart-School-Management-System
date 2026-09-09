package com.smartschool.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequeteEleve {
        @NotBlank
        private String matricule;

        @NotBlank
        private String prenom;

        @NotBlank
        private String nom;

        @Email @NotBlank
        private String email;

        private String telephone;
        private LocalDate dateNaissance;
        private String adresse;

        @NotNull
        private Long classeId;

        private Long parentId;
        private boolean actif;
}
