package com.smartschool.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ErreurApiDto {

    private LocalDateTime date;
    private int statut;
    private String message;
    private Map<String, String> erreurs;
}