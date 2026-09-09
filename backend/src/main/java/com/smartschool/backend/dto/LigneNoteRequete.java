package com.smartschool.backend.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneNoteRequete {

    @NotNull
    private Long eleveId;

    @DecimalMin("0") @DecimalMax("20")
    private double valeur;

    private String commentaire;
}
