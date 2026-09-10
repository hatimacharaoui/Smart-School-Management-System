package com.smartschool.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HoraireEmploiDuTempDto {
    private Long id;

    @NotBlank
    @Size(max = 30)
    private String jour;

    @NotNull
    private LocalTime heureDebut;

    @NotNull
    private Long matiereId;

    @NotNull
    private Long classeId;

    @NotNull
    private Long enseignantId;

    @NotBlank
    @Size(max = 50)
    private String salle;

}
