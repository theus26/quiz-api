package com.mathe.quiz_biblico_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
        name = "conjunto",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "pergunta_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class Conjunto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pergunta_id", nullable = false)
    private Pergunta pergunta;

    @Column(name = "visto_em", nullable = false)
    private Instant vistoEm;

    @PrePersist
    void preencherVistoEm() {
        if (vistoEm == null) {
            vistoEm = Instant.now();
        }
    }
}
