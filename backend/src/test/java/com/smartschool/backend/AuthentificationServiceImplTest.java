package com.smartschool.backend;

import java.util.Optional;

import com.smartschool.backend.dto.AuthentificationDto;
import com.smartschool.backend.entity.Role;
import com.smartschool.backend.entity.User;
import com.smartschool.backend.repository.UserRepository;
import com.smartschool.backend.security.JwtService;
import com.smartschool.backend.service.impl.AuthentificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthentificationServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthentificationServiceImpl authentificationService;

    @Test
    void doitConnecterUnUtilisateurEtRetournerSonToken() {
        AuthentificationDto demande = AuthentificationDto.builder()
                .email("admin@smartschool.com")
                .motDePasse("12345")
                .build();

        User user = new User();
        user.setId(1L);
        user.setNomComplet("Mohammed Alaoui");
        user.setEmail("admin@smartschool.com");
        user.setRole(Role.ADMINISTRATEUR);

        when(userRepository.findByEmail("admin@smartschool.com"))
                .thenReturn(Optional.of(user));
        when(jwtService.generateToken("admin@smartschool.com", "ADMINISTRATEUR"))
                .thenReturn("token-jwt-test");

        AuthentificationDto resultat = authentificationService.connecter(demande);

        assertEquals(1L, resultat.getId());
        assertEquals("Mohammed Alaoui", resultat.getNomComplet());
        assertEquals("token-jwt-test", resultat.getToken());
        assertEquals(Role.ADMINISTRATEUR, resultat.getRole());
        verify(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
