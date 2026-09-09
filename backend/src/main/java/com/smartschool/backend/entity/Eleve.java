package com.smartschool.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "eleve")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Eleve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String matricule;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String email;

    private String telephone;

    private LocalDate dateNaissance;

    private String adresse;

    @Column(nullable = false)
    private Long classeId;

    @Column(nullable = false)
    private Long parentId;

    @Column(nullable = false)
    private boolean actif;

}
