package com.mathe.quiz_biblico_api.model;

import com.mathe.quiz_biblico_api.Enum.FiltroTestamento;
import com.mathe.quiz_biblico_api.Enum.Modalidade;
import com.mathe.quiz_biblico_api.Enum.Nivel;
import com.mathe.quiz_biblico_api.Enum.ResultadoPartida;
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

    @Enumerated(EnumType.STRING)
    @Column(length = 12)
    private Modalidade modalidade;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Nivel nivel;

    private Integer respondidas;

    private Integer erros;

    @Enumerated(EnumType.STRING)
    @Column(length = 12)
    private ResultadoPartida resultado;

    @PrePersist
    void preencherInicio() {
        if (iniciadaEm == null) {
            iniciadaEm = Instant.now();
        }
    }
}
