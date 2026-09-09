package com.smartschool.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Entity
@Table(name = "parent")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Parent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String email;

    private String telephone;

    @Column(nullable = false)
    private boolean actif;

}
