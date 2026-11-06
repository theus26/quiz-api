package com.mathe.quiz_biblico_api.model;

import com.mathe.quiz_biblico_api.Enum.FiltroTestamento;
import com.mathe.quiz_biblico_api.Enum.Modalidade;
import com.mathe.quiz_biblico_api.Enum.Nivel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "rodada")
@Getter
@Setter
@NoArgsConstructor
public class Rodada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "filtro_testamento", nullable = false, length = 10)
    private FiltroTestamento filtroTestamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private Modalidade modalidade;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Nivel nivel;

    @Column(name = "criada_em", nullable = false)
    private Instant criadaEm;

    @Column(nullable = false)
    private boolean consumida = false;

    @Column(nullable = false)
    private int revelacoes = 0;

    @PrePersist
    void preencherCriacao() {
        if (criadaEm == null) {
            criadaEm = Instant.now();
        }
    }
}
