package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.NotificationDto;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.NotificationRepository;
import com.smartschool.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final Mappers mappers;


    public Page<NotificationDto> chercherParUtilisateur(Long id, String recherche, Boolean lue, Pageable pageable) {

        return notificationRepository.rechercher(id, recherche, lue, pageable)
                .map(mappers::toDto);
    }


}
