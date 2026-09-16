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

    @NotNull(message = "Ce champ est obligatoire.")
    private Long eleveId;

    @NotNull(message = "Ce champ est obligatoire.")
    private Long parentId;

    @Positive(message = "La valeur doit être strictement positive.")
    private double montant;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(max = 80, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String methode;

    @NotNull(message = "Ce champ est obligatoire.")
    private LocalDate date;

    @NotNull(message = "Ce champ est obligatoire.")
    private StatutPaiement statut;

    @Size(max = 500, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String urlJustificatif;
}
