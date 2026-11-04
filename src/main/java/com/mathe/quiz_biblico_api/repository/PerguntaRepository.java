package com.mathe.quiz_biblico_api.repository;

import com.mathe.quiz_biblico_api.Enum.Nivel;
import com.mathe.quiz_biblico_api.Enum.Testamento;
import com.mathe.quiz_biblico_api.model.Pergunta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {

    @Query("""
            SELECT p FROM Pergunta p
            WHERE p.ativa = true
              AND p.nivel = :nivel
              AND EXISTS (SELECT 1 FROM Alternativa a WHERE a.pergunta.id = p.id)
              AND p.id NOT IN :excluirIds
            ORDER BY function('random')
            """)
    List<Pergunta> sortearPorNivel(
            @Param("nivel") Nivel nivel,
            @Param("excluirIds") Collection<Long> excluirIds,
            Pageable pageable);

    @Query("""
            SELECT p FROM Pergunta p
            WHERE p.ativa = true
              AND p.nivel = :nivel
              AND p.testamento = :testamento
              AND EXISTS (SELECT 1 FROM Alternativa a WHERE a.pergunta.id = p.id)
              AND p.id NOT IN :excluirIds
            ORDER BY function('random')
            """)
    List<Pergunta> sortearPorNivelETestamento(
            @Param("nivel") Nivel nivel,
            @Param("testamento") Testamento testamento,
            @Param("excluirIds") Collection<Long> excluirIds,
            Pageable pageable);
}
