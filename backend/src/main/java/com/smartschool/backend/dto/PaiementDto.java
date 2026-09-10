package com.smartschool.backend.dto;

import com.smartschool.backend.entity.StatutPaiement;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaiementDto {
    private Long id;

    @NotNull
    private Long eleveId;

    @NotNull
    private Long parentId;

    @Positive
    private double montant;

    @NotBlank
    @Size(max = 80)
    private String methode;

    @NotNull
    private LocalDate date;

    @NotNull
    private StatutPaiement statut;

    @Size(max = 500)
    private String urlJustificatif;
}
