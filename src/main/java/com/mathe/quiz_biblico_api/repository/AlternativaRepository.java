package com.mathe.quiz_biblico_api.repository;

import com.mathe.quiz_biblico_api.model.Alternativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlternativaRepository extends JpaRepository<Alternativa, Long> {

    List<Alternativa> findByPerguntaIdOrderByOrdemAsc(Long perguntaId);

    @Query("""
            SELECT a FROM Alternativa a
            WHERE a.pergunta.id IN :perguntaIds
            ORDER BY a.pergunta.id, a.ordem
            """)
    List<Alternativa> findByPerguntaIdIn(@Param("perguntaIds") Collection<Long> perguntaIds);

    @Query("""
            SELECT a.correta FROM Alternativa a
            WHERE a.id = :alternativaId AND a.pergunta.id = :perguntaId
            """)
    Optional<Boolean> buscarCorreta(@Param("perguntaId") Long perguntaId, @Param("alternativaId") Long alternativaId);

    void deleteByPerguntaId(Long perguntaId);
}
