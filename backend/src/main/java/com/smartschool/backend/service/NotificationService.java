package com.smartschool.backend.service;

import com.smartschool.backend.dto.NotificationDto;
import com.smartschool.backend.entity.Role;
import com.smartschool.backend.entity.TypeNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

    Page<NotificationDto> chercherParUtilisateur(Long id, String recherche, Boolean lue, Pageable pageable);

    Page<NotificationDto> chercherParDestinataire(
            Long destinataireId,
            String recherche,
            Boolean lue,
            Pageable pagination
    );
    NotificationDto creer(NotificationDto notification);

    NotificationDto marquerCommeLue(Long id);

    void notifierRole(Role role, String titre, String message, TypeNotification type, Long entiteLieeId);
}
