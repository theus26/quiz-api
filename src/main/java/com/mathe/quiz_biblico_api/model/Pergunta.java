package com.mathe.quiz_biblico_api.model;

import com.mathe.quiz_biblico_api.Enum.Nivel;
import com.mathe.quiz_biblico_api.Enum.Testamento;
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

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public Testamento getTestamento() {
        return testamento;
    }

    public void setTestamento(Testamento testamento) {
        this.testamento = testamento;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Pergunta(String enunciado, Testamento testamento, Nivel nivel, String referencia ){
        this.enunciado = enunciado;
        this.testamento = testamento;
        this.nivel = nivel;
        this.referencia = referencia;
    }

    @PrePersist
    void preencherCriacao() {
        if (criadaEm == null) {
            criadaEm = Instant.now();
        }
    }
}
