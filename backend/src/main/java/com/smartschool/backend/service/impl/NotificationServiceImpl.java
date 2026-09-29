package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.NotificationDto;
import com.smartschool.backend.entity.Notification;
import com.smartschool.backend.entity.Role;
import com.smartschool.backend.entity.TypeNotification;
import com.smartschool.backend.entity.User;
import com.smartschool.backend.exception.ResourceNotFoundException;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.NotificationRepository;
import com.smartschool.backend.repository.UserRepository;
import com.smartschool.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final Mappers mappers;


    public Page<NotificationDto> chercherParUtilisateur(Long id, String recherche, Boolean lue, Pageable pageable) {

        return notificationRepository.rechercher(id, recherche, lue, pageable)
                .map(mappers::toDto);
    }


    @CacheEvict(value = "notifications", allEntries = true)
    public void notifierRole(Role role, String titre, String message, TypeNotification type, Long entiteLieeId) {

        List<User> destinataires = userRepository.findByRole(role);

        for (User destinataire : destinataires) {
            notifierUtilisateur(destinataire.getId(), titre, message, type, entiteLieeId);
        }
    }


    @CacheEvict(value = "notifications", allEntries = true)
    public NotificationDto creer(NotificationDto dto) {
        Notification notification = mappers.toEntite(dto);
        if (notification.getDateCreation() == null) {
            notification.setDateCreation(LocalDateTime.now());
        }
        return mappers.toDto(notificationRepository.save(notification));
    }

    @CacheEvict(value = "notifications", allEntries = true)
    public NotificationDto marquerCommeLue(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification introuvable"));
        notification.setLue(true);
        return mappers.toDto(notificationRepository.save(notification));
    }


    @CacheEvict(value = "notifications", allEntries = true)
    public void notifierUtilisateur(Long destinataireId, String titre, String message, TypeNotification type, Long entiteLieeId) {

        if (!userRepository.existsById(destinataireId)) {
            throw new ResourceNotFoundException("Destinataire introuvable.");
        }

        Notification notification = Notification.builder()
                .destinataireId(destinataireId)
                .titre(titre)
                .message(message)
                .type(type)
                .dateCreation(LocalDateTime.now())
                .lue(false)
                .entiteLieeId(entiteLieeId)
                .build();

        notificationRepository.save(notification);
    }

}
