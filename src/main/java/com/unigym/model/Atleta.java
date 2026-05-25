package com.unigym.model;

import com.unigym.model.enums.Objetivo;
import com.unigym.model.enums.NivelExperiencia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "atletas")
public class Atleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false)
    private Double peso;

    @Column(nullable = false)
    private Double altura;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Objetivo objetivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelExperiencia nivelExperiencia;

    private String restricoesFisicas;

    @Column(nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();
}