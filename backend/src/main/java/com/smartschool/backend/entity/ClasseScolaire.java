package com.smartschool.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "classe_scolaire")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClasseScolaire {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(nullable = false)
    private String niveau;

    private Long enseignantPrincipalId;
}
