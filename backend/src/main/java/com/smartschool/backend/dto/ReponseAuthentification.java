package com.smartschool.backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReponseAuthentification {

    private String token;
    private Long id;
    private Long referenceId;
    private String nomComplet;
    private String email;
    private String telephone;
    private String role;
}
