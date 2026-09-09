package com.smartschool.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "creneau_emploi_du_temps")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreneauEmploiDuTemps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String jour;

    @Column(nullable = false)
    private LocalTime heureDebut;

    @Column(nullable = false)
    private Long matiereId;

    @Column(nullable = false)
    private Long classeId;

    @Column(nullable = false)
    private Long enseignantId;

    @Column(nullable = false)
    private String salle;
}
