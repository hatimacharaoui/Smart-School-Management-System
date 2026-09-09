package com.smartschool.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "presence")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Presence {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long eleveId;

    @Column(nullable = false)
    private Long classeId;

    @Column(nullable = false)
    private Long matiereId;

    @Column(nullable = false)
    private Long enseignantId;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPresence statut;
}
