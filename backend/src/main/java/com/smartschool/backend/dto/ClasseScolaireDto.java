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

    @NotBlank
    @Size(max = 30)
    private String nom;

    @NotBlank
    @Size(max = 80)
    private String niveau;

    private Long enseignantPrincipalId;
}
