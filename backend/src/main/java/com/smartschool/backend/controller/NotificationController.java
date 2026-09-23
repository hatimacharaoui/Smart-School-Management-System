package com.smartschool.backend.controller;

import com.smartschool.backend.dto.NotificationDto;
import com.smartschool.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;


    @GetMapping("utilisateur/{id}")
    public ResponseEntity<Page<NotificationDto>> chercherParUtilisateur(
            @PathVariable Long id, @RequestParam(required = false) String recherche,
            @RequestParam(required = false) Boolean lue, Pageable pageable
    ) {
        return ResponseEntity.ok(notificationService.chercherParUtilisateur(id, recherche, lue, pageable));
    }

}
