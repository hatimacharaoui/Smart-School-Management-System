package com.smartschool.backend.service;

import com.smartschool.backend.dto.PresenceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PresenceService {

    Page<PresenceDto> chercherParEleve(Long eleveId, Pageable pageable);

    Page<PresenceDto> afficherPresence(Pageable pagination);

    Page<PresenceDto> chercherParClasseEtDate(Long classeId, LocalDate date, Pageable pagination);

    PresenceDto enregistrer(PresenceDto presence);

    List<PresenceDto> enregistrerTout(List<PresenceDto> presences);

    PresenceDto creer(PresenceDto dto);

    PresenceDto modifier(Long id, PresenceDto dto);

    void supprimer(Long id);
}
