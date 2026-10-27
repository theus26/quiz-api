package com.mathe.quiz_biblico_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "alternativa",
        uniqueConstraints = @UniqueConstraint(columnNames = {"pergunta_id", "ordem"})
)
@Getter
@Setter
@NoArgsConstructor
public class Alternativa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pergunta_id", nullable = false)
    private Pergunta pergunta;

    @Column(nullable = false)
    private String texto;

    @Column(nullable = false)
    private boolean correta = false;

    @Column(nullable = false)
    private short ordem;
}
