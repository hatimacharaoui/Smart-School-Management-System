package com.smartschool.backend;


import com.smartschool.backend.dto.DevoirDto;
import com.smartschool.backend.entity.Devoir;
import com.smartschool.backend.entity.Role;
import com.smartschool.backend.entity.StatutDevoir;
import com.smartschool.backend.entity.TypeNotification;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.DevoirRepository;
import com.smartschool.backend.service.NotificationService;
import com.smartschool.backend.service.impl.DevoirServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DevoirServiceImplTest {

    @Mock
    private DevoirRepository devoirRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private Mappers mappers;

    @InjectMocks
    private DevoirServiceImpl devoirService;

    @Test
    void doitCreerEtEnregistrerUnDevoir() {
        DevoirDto demande = DevoirDto.builder()
                .titre("Contrôle de mathématiques")
                .matiereId(1L)
                .classeId(1L)
                .enseignantId(2L)
                .dateLimite(LocalDate.now().plusDays(7))
                .statut(StatutDevoir.A_VENIR)
                .build();

        Devoir devoir = Devoir.builder()
                .titre("Contrôle de mathématiques")
                .matiereId(1L)
                .classeId(1L)
                .enseignantId(2L)
                .dateLimite(demande.getDateLimite())
                .statut(StatutDevoir.A_VENIR)
                .build();

        Devoir devoirEnregistre = Devoir.builder()
                .id(8L)
                .titre("Contrôle de mathématiques")
                .matiereId(1L)
                .classeId(1L)
                .enseignantId(2L)
                .dateLimite(demande.getDateLimite())
                .statut(StatutDevoir.A_VENIR)
                .build();

        DevoirDto reponse = DevoirDto.builder()
                .id(8L)
                .titre("Contrôle de mathématiques")
                .classeId(1L)
                .enseignantId(2L)
                .build();

        when(mappers.toEntite(demande)).thenReturn(devoir);
        when(devoirRepository.save(devoir)).thenReturn(devoirEnregistre);
        when(mappers.toDto(devoirEnregistre)).thenReturn(reponse);

        DevoirDto resultat = devoirService.creer(demande);

        assertEquals(8L, resultat.getId());
        verify(devoirRepository).save(devoir);
        verify(mappers).toDto(devoirEnregistre);
    }

    @Test
    void doitModifierLeStatutDuDevoir() {
        Devoir devoir = Devoir.builder()
                .id(8L)
                .titre("Contrôle de mathématiques")
                .statut(StatutDevoir.A_VENIR)
                .build();

        DevoirDto reponse = DevoirDto.builder()
                .id(8L)
                .titre("Contrôle de mathématiques")
                .statut(StatutDevoir.EN_CORRECTION)
                .build();

        when(devoirRepository.findById(8L)).thenReturn(Optional.of(devoir));
        when(devoirRepository.save(devoir)).thenReturn(devoir);
        when(mappers.toDto(devoir)).thenReturn(reponse);

        DevoirDto resultat = devoirService.modifierStatut(
                8L,
                StatutDevoir.EN_CORRECTION
        );

        assertEquals(StatutDevoir.EN_CORRECTION, devoir.getStatut());
        assertEquals(StatutDevoir.EN_CORRECTION, resultat.getStatut());

        verify(devoirRepository).findById(8L);
        verify(devoirRepository).save(devoir);
        verify(notificationService).notifierRole(
                eq(Role.ENSEIGNANT),
                eq("Devoir en correction"),
                anyString(),
                eq(TypeNotification.DEVOIR),
                eq(8L)
        );
    }
}
