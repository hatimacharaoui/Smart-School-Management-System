package com.smartschool.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "note", uniqueConstraints = @UniqueConstraint(columnNames = {"eleve_id", "devoir_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Note {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long eleveId;

    @Column(nullable = false)
    private Long devoirId;

    @Column(nullable = false)
    private Long enseignantId;

    @Column(nullable = false)
    private double valeur;

    @Column(nullable = false)
    private double valeurMaximale;

    @Column(nullable = false)
    private LocalDate date;

    private String commentaire;


}
