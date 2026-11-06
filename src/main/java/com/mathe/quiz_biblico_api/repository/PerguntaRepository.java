package com.mathe.quiz_biblico_api.repository;

import com.mathe.quiz_biblico_api.Enum.Nivel;
import com.mathe.quiz_biblico_api.Enum.StatusPergunta;
import com.mathe.quiz_biblico_api.Enum.Testamento;
import com.mathe.quiz_biblico_api.model.Pergunta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
              AND (p.status IS NULL OR p.status = com.mathe.quiz_biblico_api.Enum.StatusPergunta.APROVADA)
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
              AND (p.status IS NULL OR p.status = com.mathe.quiz_biblico_api.Enum.StatusPergunta.APROVADA)
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

    long countByStatus(StatusPergunta status);

    long countByStatusIsNull();

    @Query("""
            SELECT COUNT(p) FROM Pergunta p
            WHERE LOWER(TRIM(p.enunciado)) = LOWER(TRIM(:enunciado))
            """)
    long contarPorEnunciado(@Param("enunciado") String enunciado);

    @Query("""
            SELECT COUNT(p) FROM Pergunta p
            WHERE LOWER(TRIM(p.enunciado)) = LOWER(TRIM(:enunciado))
              AND p.id <> :id
            """)
    long contarPorEnunciadoExceto(@Param("enunciado") String enunciado, @Param("id") Long id);

    @Query("""
            SELECT p FROM Pergunta p
            LEFT JOIN FETCH p.autor
            WHERE p.autor.id = :autorId
            ORDER BY p.criadaEm DESC, p.id DESC
            """)
    List<Pergunta> findByAutorId(@Param("autorId") long autorId);

    @Query("""
            SELECT p FROM Pergunta p
            LEFT JOIN FETCH p.autor
            WHERE (:status IS NULL OR p.status = :status OR (:status = com.mathe.quiz_biblico_api.Enum.StatusPergunta.APROVADA AND p.status IS NULL))
            ORDER BY p.criadaEm DESC, p.id DESC
            """)
    List<Pergunta> listarParaModeracao(@Param("status") StatusPergunta status, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Pergunta p SET p.autor = null WHERE p.autor.id = :usuarioId")
    int desvincularAutor(@Param("usuarioId") long usuarioId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Pergunta p SET p.revisor = null WHERE p.revisor.id = :usuarioId")
    int desvincularRevisor(@Param("usuarioId") long usuarioId);
}
