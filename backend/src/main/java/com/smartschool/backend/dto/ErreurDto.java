package com.smartschool.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErreurDto {
    private int statut;
    private String message;
    private LocalDateTime dateHeure;
}
