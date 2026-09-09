package com.smartschool.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "devoir")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Devoir {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Long matiereId;

    @Column(nullable = false)
    private Long classeId;

    @Column(nullable = false)
    private Long enseignantId;

    @Column(nullable = false)
    private LocalDate dateLimite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDevoir statut;


}
