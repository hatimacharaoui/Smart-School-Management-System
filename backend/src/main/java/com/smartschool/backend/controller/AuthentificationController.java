package com.smartschool.backend.controller;

import com.smartschool.backend.dto.AuthentificationDto;
import com.smartschool.backend.service.AuthentificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthentificationController {
    private final AuthentificationService authentificationService;


    @PostMapping("/connexion")
    public ResponseEntity<AuthentificationDto> connecter(@Valid @RequestBody AuthentificationDto authentification) {

        return ResponseEntity.ok(authentificationService.connecter(authentification));
    }
}
