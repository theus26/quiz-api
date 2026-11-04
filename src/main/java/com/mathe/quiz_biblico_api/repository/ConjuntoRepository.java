package com.mathe.quiz_biblico_api.repository;

import com.mathe.quiz_biblico_api.Enum.Testamento;
import com.mathe.quiz_biblico_api.model.Conjunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ConjuntoRepository extends JpaRepository<Conjunto, Long> {

    @Query("SELECT c.pergunta.id FROM Conjunto c WHERE c.usuario.id = :usuarioId")
    List<Long> findPerguntaIdsByUsuarioId(@Param("usuarioId") long usuarioId);

    @Query("""
            SELECT c.pergunta.id FROM Conjunto c
            WHERE c.usuario.id = :usuarioId
              AND c.pergunta.id IN :perguntaIds
            """)
    List<Long> findPerguntaIdsByUsuarioIdAndPerguntaIdIn(
            @Param("usuarioId") long usuarioId,
            @Param("perguntaIds") Collection<Long> perguntaIds);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Conjunto c WHERE c.usuario.id = :usuarioId")
    int deleteByUsuarioId(@Param("usuarioId") long usuarioId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            DELETE FROM Conjunto c
            WHERE c.usuario.id = :usuarioId
              AND c.pergunta.testamento = :testamento
            """)
    int deleteByUsuarioIdAndPerguntaTestamento(
            @Param("usuarioId") long usuarioId,
            @Param("testamento") Testamento testamento);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Conjunto c WHERE c.pergunta.id = :perguntaId")
    int deleteByPerguntaId(@Param("perguntaId") long perguntaId);
}
