package com.smartschool.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequeteConnexion {

        @Email @NotBlank
        private String email;

        @NotBlank
        private String motDePasse;
}
