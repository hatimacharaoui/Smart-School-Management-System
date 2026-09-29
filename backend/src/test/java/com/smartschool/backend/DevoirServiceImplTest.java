package com.smartschool.backend;

import com.smartschool.backend.dto.DevoirDto;
import com.smartschool.backend.entity.Devoir;
import com.smartschool.backend.entity.Eleve;
import com.smartschool.backend.entity.StatutDevoir;
import com.smartschool.backend.entity.TypeNotification;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.DevoirRepository;
import com.smartschool.backend.repository.EleveRepository;
import com.smartschool.backend.service.NotificationService;
import com.smartschool.backend.service.impl.DevoirServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DevoirServiceImplTest {

    @Mock
    private DevoirRepository devoirRepository;

    @Mock
    private EleveRepository eleveRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private Mappers mappers;

    @InjectMocks
    private DevoirServiceImpl devoirService;

    private Devoir devoir;

    @BeforeEach
    void setUp() {

        devoir = new Devoir();

        devoir.setId(8L);
        devoir.setTitre("Contrôle de mathématiques");
        devoir.setClasseId(3L);
        devoir.setEnseignantId(2L);
        devoir.setStatut(StatutDevoir.A_VENIR);
    }


    @Test
    void doitModifierLeStatutDuDevoirEnCorrection() {

        when(devoirRepository.findById(8L))
                .thenReturn(Optional.of(devoir));

        when(devoirRepository.save(any(Devoir.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(mappers.toDto(any(Devoir.class)))
                .thenReturn(new DevoirDto());


        devoirService.modifierStatut(
                8L,
                StatutDevoir.EN_CORRECTION
        );


        assertEquals(
                StatutDevoir.EN_CORRECTION,
                devoir.getStatut()
        );

        verify(devoirRepository).save(devoir);

        verify(notificationService).notifierUtilisateur(
                2L,
                "Devoir en correction",
                "Le devoir \"Contrôle de mathématiques\" est prêt à être corrigé.",
                TypeNotification.DEVOIR,
                8L
        );
    }


    @Test
    void doitNotifierElevesEtParentsQuandDevoirCorrige() {

        Eleve eleve1 = new Eleve();
        eleve1.setId(10L);
        eleve1.setParentId(20L);

        Eleve eleve2 = new Eleve();
        eleve2.setId(11L);
        eleve2.setParentId(21L);


        when(devoirRepository.findById(8L))
                .thenReturn(Optional.of(devoir));

        when(devoirRepository.save(any(Devoir.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(eleveRepository.findByClasseId(3L))
                .thenReturn(List.of(eleve1, eleve2));

        when(mappers.toDto(any(Devoir.class)))
                .thenReturn(new DevoirDto());


        devoirService.modifierStatut(
                8L,
                StatutDevoir.CORRIGE
        );


        assertEquals(
                StatutDevoir.CORRIGE,
                devoir.getStatut()
        );

        verify(eleveRepository).findByClasseId(3L);


        // Élève 1
        verify(notificationService).notifierUtilisateur(
                10L,
                "Devoir corrigé",
                "Le devoir \"Contrôle de mathématiques\" a été corrigé.",
                TypeNotification.DEVOIR,
                8L
        );

        // Parent élève 1
        verify(notificationService).notifierUtilisateur(
                20L,
                "Devoir corrigé",
                "Le devoir \"Contrôle de mathématiques\" de votre enfant a été corrigé.",
                TypeNotification.DEVOIR,
                8L
        );


        // Élève 2
        verify(notificationService).notifierUtilisateur(
                11L,
                "Devoir corrigé",
                "Le devoir \"Contrôle de mathématiques\" a été corrigé.",
                TypeNotification.DEVOIR,
                8L
        );

        // Parent élève 2
        verify(notificationService).notifierUtilisateur(
                21L,
                "Devoir corrigé",
                "Le devoir \"Contrôle de mathématiques\" de votre enfant a été corrigé.",
                TypeNotification.DEVOIR,
                8L
        );
    }
}