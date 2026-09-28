package com.smartschool.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AffectationClassesEnseignantDto {
    @NotEmpty(message = "Sélectionnez au moins une classe")
    private List<@NotNull Long> classeIds;
}
