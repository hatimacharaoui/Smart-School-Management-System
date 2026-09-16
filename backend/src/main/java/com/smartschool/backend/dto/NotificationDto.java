package com.smartschool.backend.dto;

import com.smartschool.backend.entity.TypeNotification;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Long id;

    @NotNull(message = "Ce champ est obligatoire.")
    private Long destinataireId;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(max = 180, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String titre;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(max = 1000, message = "La longueur doit être comprise entre {min} et {max} caractères.")
    private String message;

    @NotNull(message = "Ce champ est obligatoire.")
    private TypeNotification type;

    private LocalDateTime dateCreation;

    private boolean lue;

    private Long entiteLieeId;
}
