package com.smartschool.backend.mapper;

import com.smartschool.backend.dto.*;
import com.smartschool.backend.entity.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface Mappers {
    EleveDto toDto(Eleve eleve);
    Eleve toEntite(EleveDto dto);

    EnseignantDto toDto(Enseignant enseignant);
    Enseignant toEntite(EnseignantDto dto);

    ParentDto toDto(Parent parent);
    Parent toEntite(ParentDto dto);

    ClasseScolaireDto toDto(ClasseScolaire classeScolaire);
    ClasseScolaire toEntite(ClasseScolaireDto dto);

    MatiereDto toDto(Matiere matiere);
    Matiere toEntite(MatiereDto dto);

    ClasseEnseignantDto toDto(ClasseEnseignant classeEnseignant);

    DevoirDto toDto(Devoir devoir);
    Devoir toEntite(DevoirDto dto);

    NoteDto toDto(Note note);
    Note toEntite(NoteDto dto);

    PresenceDto toDto(Presence presence);
    Presence toEntite(PresenceDto dto);

    PaiementDto toDto(Paiement paiement);
    Paiement toEntite(PaiementDto dto);

    HoraireEmploiDuTempDto toDto(HoraireEmploiDuTemp horaire);
    HoraireEmploiDuTemp toEntite(HoraireEmploiDuTempDto dto);

    NotificationDto toDto(Notification notification);
    Notification toEntite(NotificationDto dto);

    UserDto toDto(User user);
    User toEntite(UserDto dto);

}
