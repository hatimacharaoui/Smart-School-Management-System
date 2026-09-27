package com.smartschool.backend;


import com.smartschool.backend.dto.EnseignantDto;
import com.smartschool.backend.entity.Enseignant;
import com.smartschool.backend.entity.Role;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.*;
import com.smartschool.backend.service.impl.EnseignantServiceImpl;
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
class EnseignantServiceImplTest {

    @Mock
    private EnseignantRepository enseignantRepository;

    @Mock
    private ClasseEnseignantRepository classeEnseignantRepository;

    @Mock
    private ClasseScolaireRepository classeScolaireRepository;

    @Mock
    private DevoirRepository devoirRepository;

    @Mock
    private PresenceRepository presenceRepository;

    @Mock
    private HoraireEmploiDuTempRepository horaireRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Mappers mappers;

    @InjectMocks
    private EnseignantServiceImpl enseignantService;

    @Test
    void doitEnregistrerUnEnseignant() {
        EnseignantDto demande = EnseignantDto.builder()
                .prenom("Hatim")
                .nom("Acharaoui")
                .email("hatim.acharaoui@smartschool.ma")
                .motDePasse("12345")
                .telephone("0612345678")
                .matiereId(1L)
                .build();

        Enseignant enseignant = new Enseignant();
        enseignant.setPrenom("Hatim");
        enseignant.setNom("Acharaoui");
        enseignant.setEmail("hatim.acharaoui@smartschool.ma");
        enseignant.setTelephone("0612345678");
        enseignant.setMatiereId(1L);

        Enseignant enseignantEnregistre = new Enseignant();
        enseignantEnregistre.setId(10L);
        enseignantEnregistre.setPrenom("Hatim");
        enseignantEnregistre.setNom("Acharaoui");
        enseignantEnregistre.setEmail("hatim.acharaoui@smartschool.ma");
        enseignantEnregistre.setTelephone("0612345678");
        enseignantEnregistre.setMatiereId(1L);
        enseignantEnregistre.setNomComplet("Hatim Acharaoui");
        enseignantEnregistre.setMotDePasse("mot-de-passe-encode");
        enseignantEnregistre.setRole(Role.ENSEIGNANT);
        enseignantEnregistre.setActif(true);

        EnseignantDto reponse = EnseignantDto.builder()
                .id(10L)
                .prenom("Hatim")
                .nom("Acharaoui")
                .email("hatim.acharaoui@smartschool.ma")
                .telephone("0612345678")
                .matiereId(1L)
                .actif(true)
                .build();

        when(mappers.toEntite(demande)).thenReturn(enseignant);
        when(passwordEncoder.encode("12345")).thenReturn("mot-de-passe-encode");
        when(enseignantRepository.save(enseignant)).thenReturn(enseignantEnregistre);
        when(mappers.toDto(enseignantEnregistre)).thenReturn(reponse);

        EnseignantDto resultat = enseignantService.enregistrer(demande);

        assertEquals(10L, resultat.getId());
        assertEquals("Hatim", resultat.getPrenom());
        assertEquals("Hatim Acharaoui", enseignant.getNomComplet());
        assertEquals("mot-de-passe-encode", enseignant.getMotDePasse());
        assertEquals(Role.ENSEIGNANT, enseignant.getRole());
        assertTrue(enseignant.isActif());

        verify(passwordEncoder).encode("12345");
        verify(enseignantRepository).save(enseignant);
    }

    @Test
    void doitRetournerUnEnseignantParSonIdentifiant() {
        Enseignant enseignant = new Enseignant();
        enseignant.setId(10L);
        enseignant.setPrenom("Hatim");
        enseignant.setNom("Acharaoui");
        enseignant.setMatiereId(1L);

        EnseignantDto reponse = EnseignantDto.builder()
                .id(10L)
                .prenom("Hatim")
                .nom("Acharaoui")
                .matiereId(1L)
                .build();

        when(enseignantRepository.findById(10L)).thenReturn(Optional.of(enseignant));
        when(mappers.toDto(enseignant)).thenReturn(reponse);

        EnseignantDto resultat = enseignantService.chercherParId(10L);

        assertEquals(10L, resultat.getId());
        assertEquals("Hatim", resultat.getPrenom());
        assertEquals("Acharaoui", resultat.getNom());
        assertEquals(1L, resultat.getMatiereId());

        verify(enseignantRepository).findById(10L);
        verify(mappers).toDto(enseignant);
    }
}
