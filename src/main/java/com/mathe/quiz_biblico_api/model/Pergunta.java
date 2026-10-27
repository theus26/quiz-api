package com.mathe.quiz_biblico_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pergunta")
@Getter
@Setter
@NoArgsConstructor
public class Pergunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String enunciado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Testamento testamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Nivel nivel;

    @Column(length = 120)
    private String referencia;

    @Column(nullable = false)
    private boolean ativa = true;

    @Column(name = "criada_em", nullable = false)
    private Instant criadaEm;

    @OneToMany(mappedBy = "pergunta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alternativa> alternativas = new ArrayList<>();

    @PrePersist
    void preencherCriacao() {
        if (criadaEm == null) {
            criadaEm = Instant.now();
        }
    }
}
