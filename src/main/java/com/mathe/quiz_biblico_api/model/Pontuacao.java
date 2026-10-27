package com.mathe.quiz_biblico_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "pontuacao")
@Getter
@Setter
@NoArgsConstructor
public class Pontuacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "filtro_testamento", nullable = false, length = 10)
    private FiltroTestamento filtroTestamento;

    @Column(nullable = false)
    private int acertos;

    @Column(nullable = false)
    private int pontos;

    @Column(name = "iniciada_em", nullable = false)
    private Instant iniciadaEm;

    @Column(name = "finalizada_em")
    private Instant finalizadaEm;

    @PrePersist
    void preencherInicio() {
        if (iniciadaEm == null) {
            iniciadaEm = Instant.now();
        }
    }
}
