package com.smartschool.backend.service;

import com.smartschool.backend.dto.NotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

    Page<NotificationDto> chercherParUtilisateur(Long id, String recherche, Boolean lue, Pageable pageable);
}
