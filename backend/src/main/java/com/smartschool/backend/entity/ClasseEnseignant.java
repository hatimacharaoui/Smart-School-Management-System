package com.smartschool.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "classe_enseignant", uniqueConstraints = @UniqueConstraint(columnNames = {"enseignant_id", "classe_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClasseEnseignant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long enseignantId;

    @Column(nullable = false)
    private Long classeId;
}
