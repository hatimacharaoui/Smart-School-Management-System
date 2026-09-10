package com.smartschool.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartschool.backend.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank
    @Size(max = 150)
    private String nomComplet;

    @Email
    @NotBlank
    @Size(max = 180)
    private String email;

    private Role role;

    @Size(max = 30)
    private String telephone;

    private Long referenceId;

    private boolean actif;
}
