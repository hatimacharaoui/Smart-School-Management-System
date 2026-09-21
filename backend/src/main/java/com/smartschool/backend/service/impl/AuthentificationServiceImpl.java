package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.AuthentificationDto;
import com.smartschool.backend.entity.User;
import com.smartschool.backend.repository.UserRepository;
import com.smartschool.backend.security.JwtService;
import com.smartschool.backend.service.AuthentificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthentificationServiceImpl implements AuthentificationService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;


    public AuthentificationDto connecter(AuthentificationDto authentification) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authentification.getEmail(),
                        authentification.getMotDePasse()
                ));

        User user = userRepository.findByEmail(authentification.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Indentifiants incorrects"));
        String token  = jwtService.generateToken(user.getEmail(), user.getRole().name());

        return AuthentificationDto.builder()
                .id(user.getId())
                .nomComplet(user.getNomComplet())
                .email(user.getEmail())
                .token(token)
                .telephone(user.getTelephone())
                .role(user.getRole().name())
                .build();
    }

}
