package com.smartschool.backend.mapper;

import com.smartschool.backend.dto.*;
import com.smartschool.backend.entity.*;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface Mappers {
    EleveDto toDto(Eleve eleve);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nomComplet", ignore = true)
    @Mapping(target = "motDePasse", ignore = true)
    @Mapping(target = "role", ignore = true)
    Eleve toEntite(EleveDto dto);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nomComplet", ignore = true)
    @Mapping(target = "motDePasse", ignore = true)
    @Mapping(target = "role", ignore = true)
    void update(EleveDto dto, @MappingTarget Eleve eleve);


    EnseignantDto toDto(Enseignant enseignant);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nomComplet", ignore = true)
    @Mapping(target = "motDePasse", ignore = true)
    @Mapping(target = "role", ignore = true)
    Enseignant toEntite(EnseignantDto dto);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nomComplet", ignore = true)
    @Mapping(target = "motDePasse", ignore = true)
    @Mapping(target = "role", ignore = true)
    void update(EnseignantDto dto, @MappingTarget Enseignant enseignant);


    ParentDto toDto(Parent parent);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nomComplet", ignore = true)
    @Mapping(target = "motDePasse", ignore = true)
    @Mapping(target = "role", ignore = true)
    Parent toEntite(ParentDto dto);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nomComplet", ignore = true)
    @Mapping(target = "motDePasse", ignore = true)
    @Mapping(target = "role", ignore = true)
    void update(ParentDto dto, @MappingTarget Parent parent);


    ClasseScolaireDto toDto(ClasseScolaire classeScolaire);
    @Mapping(target = "id", ignore = true)
    ClasseScolaire toEntite(ClasseScolaireDto dto);
    @Mapping(target = "id", ignore = true)
    void update(ClasseScolaireDto dto, @MappingTarget ClasseScolaire classeScolaire);


    MatiereDto toDto(Matiere matiere);
    @Mapping(target = "id", ignore = true)
    Matiere toEntite(MatiereDto dto);
    @Mapping(target = "id", ignore = true)
    void update(MatiereDto dto, @MappingTarget Matiere matiere);


    ClasseEnseignantDto toDto(ClasseEnseignant classeEnseignant);
    ClasseEnseignant toEntite(ClasseEnseignantDto dto);

    DevoirDto toDto(Devoir devoir);
    @Mapping(target = "id", ignore = true)
    Devoir toEntite(DevoirDto dto);
    @Mapping(target = "id", ignore = true)
    void update(DevoirDto dto, @MappingTarget Devoir devoir);


    NoteDto toDto(Note note);
    @Mapping(target = "id", ignore = true)
    Note toEntite(NoteDto dto);
    @Mapping(target = "id", ignore = true)
    void update(NoteDto dto, @MappingTarget Note note);


    PresenceDto toDto(Presence presence);
    @Mapping(target = "id", ignore = true)
    Presence toEntite(PresenceDto dto);
    @Mapping(target = "id", ignore = true)
    void update(PresenceDto dto, @MappingTarget Presence presence);


    PaiementDto toDto(Paiement paiement);
    @Mapping(target = "id", ignore = true)
    Paiement toEntite(PaiementDto dto);
    @Mapping(target = "id", ignore = true)
    void update(PaiementDto dto, @MappingTarget Paiement paiement);


    HoraireEmploiDuTempDto toDto(HoraireEmploiDuTemp horaire);
    @Mapping(target = "id", ignore = true)
    HoraireEmploiDuTemp toEntite(HoraireEmploiDuTempDto dto);
    @Mapping(target = "id", ignore = true)
    void update(HoraireEmploiDuTempDto dto, @MappingTarget HoraireEmploiDuTemp horaire);


    NotificationDto toDto(Notification notification);
    @Mapping(target = "id", ignore = true)
    Notification toEntite(NotificationDto dto);
    @Mapping(target = "id", ignore = true)
    void update(NotificationDto dto, @MappingTarget Notification notification);

    UserDto toDto(User user);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "motDePasse", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "actif", ignore = true)
    User toEntite(UserDto dto);
    void update(UserDto dto, @MappingTarget User user);

}
