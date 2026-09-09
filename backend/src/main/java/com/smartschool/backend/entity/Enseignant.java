package com.smartschool.backend.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "enseignant")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Enseignant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String email;

    private String telephone;

    @Column(nullable = false)
    private Long matiereId;

    @Column(nullable = false)
    private boolean actif;

}
