package com.smartschool.backend;

import com.smartschool.backend.dto.ParentDto;
import com.smartschool.backend.entity.Parent;
import com.smartschool.backend.entity.Role;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.EleveRepository;
import com.smartschool.backend.repository.PaiementRepository;
import com.smartschool.backend.repository.ParentRepository;
import com.smartschool.backend.service.impl.ParentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParentServiceImplTest {

    @Mock
    private ParentRepository parentRepository;

    @Mock
    private EleveRepository eleveRepository;

    @Mock
    private PaiementRepository paiementRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Mappers mappers;

    @InjectMocks
    private ParentServiceImpl parentService;

    @Test
    void doitEnregistrerUnParent() {
        ParentDto demande = ParentDto.builder()
                .prenom("Ahmed")
                .nom("Acharaoui")
                .email("ahmed.acharaoui@smartschool.com")
                .motDePasse("12345")
                .telephone("0612345678")
                .build();

        Parent parent = new Parent();
        parent.setPrenom("Ahmed");
        parent.setNom("Acharaoui");
        parent.setEmail("ahmed.acharaoui@smartschool.com");
        parent.setTelephone("0612345678");

        Parent parentEnregistre = new Parent();
        parentEnregistre.setId(20L);
        parentEnregistre.setPrenom("Ahmed");
        parentEnregistre.setNom("Acharaoui");
        parentEnregistre.setEmail("ahmed.acharaoui@smartschool.com");
        parentEnregistre.setTelephone("0612345678");
        parentEnregistre.setNomComplet("Ahmed Acharaoui");
        parentEnregistre.setMotDePasse("mot-de-passe-encode");
        parentEnregistre.setRole(Role.PARENT);
        parentEnregistre.setActif(true);

        ParentDto reponse = ParentDto.builder()
                .id(20L)
                .prenom("Ahmed")
                .nom("Acharaoui")
                .email("ahmed.acharaoui@smartschool.com")
                .telephone("0612345678")
                .actif(true)
                .build();

        when(mappers.toEntite(demande)).thenReturn(parent);
        when(passwordEncoder.encode("12345")).thenReturn("mot-de-passe-encode");
        when(parentRepository.save(parent)).thenReturn(parentEnregistre);
        when(mappers.toDto(parentEnregistre)).thenReturn(reponse);

        ParentDto resultat = parentService.enregistrer(demande);

        assertEquals(20L, resultat.getId());
        assertEquals("Ahmed", resultat.getPrenom());
        assertEquals("Ahmed Acharaoui", parent.getNomComplet());
        assertEquals("mot-de-passe-encode", parent.getMotDePasse());
        assertEquals(Role.PARENT, parent.getRole());
        assertTrue(parent.isActif());

        verify(passwordEncoder).encode("12345");
        verify(parentRepository).save(parent);
    }

    @Test
    void doitRetournerUnParentParSonIdentifiant() {
        Parent parent = new Parent();
        parent.setId(20L);
        parent.setPrenom("Ahmed");
        parent.setNom("Acharaoui");

        ParentDto reponse = ParentDto.builder()
                .id(20L)
                .prenom("Ahmed")
                .nom("Acharaoui")
                .build();

        when(parentRepository.findById(20L)).thenReturn(Optional.of(parent));
        when(mappers.toDto(parent)).thenReturn(reponse);

        ParentDto resultat = parentService.chercherParId(20L);

        assertEquals(20L, resultat.getId());
        assertEquals("Ahmed", resultat.getPrenom());
        assertEquals("Acharaoui", resultat.getNom());

        verify(parentRepository).findById(20L);
        verify(mappers).toDto(parent);
    }
}
