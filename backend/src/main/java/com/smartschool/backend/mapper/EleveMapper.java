package com.smartschool.backend.mapper;

import com.smartschool.backend.dto.RequeteEleve;
import com.smartschool.backend.entity.Eleve;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface EleveMapper {
    Eleve versEntite(RequeteEleve requete);
    void mettreAJour(RequeteEleve requete, @MappingTarget Eleve eleve);
}
