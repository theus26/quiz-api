package com.mathe.quiz_biblico_api.repository;

import com.mathe.quiz_biblico_api.model.Pontuacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PontuacaoRepository extends JpaRepository<Pontuacao, Long> {

    @Query("""
            SELECT u.id, u.nome, MAX(p.pontos)
            FROM Pontuacao p
            JOIN p.usuario u
            GROUP BY u.id, u.nome
            ORDER BY MAX(p.pontos) DESC, u.nome ASC
            """)
    List<Object[]> ranking();

    @Query("""
            SELECT p FROM Pontuacao p
            JOIN FETCH p.usuario u
            WHERE u.id = :usuarioId
            ORDER BY p.finalizadaEm DESC, p.id DESC
            """)
    List<Pontuacao> findByUsuarioId(@Param("usuarioId") long usuarioId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Pontuacao p WHERE p.usuario.id = :usuarioId")
    int deleteByUsuarioId(@Param("usuarioId") long usuarioId);
}
