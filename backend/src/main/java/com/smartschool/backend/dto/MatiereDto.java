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

    @NotBlank
    @Size(max = 100)
    private String nom;

    @Min(1) @Max(10)
    private int coefficient;

    private boolean actif;
}
