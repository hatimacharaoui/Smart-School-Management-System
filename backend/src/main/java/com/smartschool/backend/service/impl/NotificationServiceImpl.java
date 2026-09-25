package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.NotificationDto;
import com.smartschool.backend.entity.Notification;
import com.smartschool.backend.entity.Role;
import com.smartschool.backend.entity.TypeNotification;
import com.smartschool.backend.entity.User;
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
            Notification notification = Notification.builder()
                    .destinataireId(destinataire.getId())
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

}
