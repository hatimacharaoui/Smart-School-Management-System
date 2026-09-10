package com.smartschool.backend.dto;

import com.smartschool.backend.entity.StatutDevoir;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevoirDto {
    private Long id;

    @NotBlank
    @Size(max = 180)
    private String titre;

    @Size(max = 1000)
    private String description;

    @NotNull
    private Long matiereId;

    @NotNull
    private Long classeId;

    @NotNull
    private Long enseignantId;

    @NotNull
    private LocalDate dateLimite;

    @NotNull
    private StatutDevoir statut;
}
