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

    @NotNull
    private Long destinataireId;

    @NotBlank
    @Size(max = 180)
    private String titre;

    @NotBlank
    @Size(max = 1000)
    private String message;

    @NotNull
    private TypeNotification type;

    private LocalDateTime dateCreation;

    private boolean lue;

    private Long entiteLieeId;
}
