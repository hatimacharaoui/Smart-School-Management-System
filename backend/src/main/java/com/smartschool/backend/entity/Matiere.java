package com.smartschool.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "matiere")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Matiere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(nullable = false)
    private int coefficient;

    @Column(nullable = false)
    private boolean actif;
}
