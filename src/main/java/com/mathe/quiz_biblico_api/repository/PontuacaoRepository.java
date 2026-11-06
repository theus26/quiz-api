package com.mathe.quiz_biblico_api.repository;

import com.mathe.quiz_biblico_api.Enum.Role;
import com.mathe.quiz_biblico_api.model.Pontuacao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PontuacaoRepository extends JpaRepository<Pontuacao, Long> {

    @Query("""
            SELECT u.id, u.nome, COALESCE(SUM(p.pontos), 0), COUNT(p.id),
                   COALESCE(SUM(p.acertos), 0),
                   COALESCE(SUM(COALESCE(p.respondidas, p.acertos)), 0),
                   COALESCE(MAX(p.pontos), 0)
            FROM Pontuacao p
            JOIN p.usuario u
            WHERE u.role <> :papel
            GROUP BY u.id, u.nome
            ORDER BY SUM(p.pontos) DESC, COUNT(p.id) DESC, u.nome ASC
            """)
    List<Object[]> ranking(@Param("papel") Role papel);

    @Query("""
            SELECT p FROM Pontuacao p
            JOIN FETCH p.usuario u
            WHERE u.id = :usuarioId
            ORDER BY p.finalizadaEm DESC, p.id DESC
            """)
    List<Pontuacao> findByUsuarioId(@Param("usuarioId") long usuarioId);

    @Query("""
            SELECT p FROM Pontuacao p
            JOIN FETCH p.usuario u
            ORDER BY p.finalizadaEm DESC, p.id DESC
            """)
    List<Pontuacao> historicoRecente(Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Pontuacao p WHERE p.usuario.id = :usuarioId")
    int deleteByUsuarioId(@Param("usuarioId") long usuarioId);
}
