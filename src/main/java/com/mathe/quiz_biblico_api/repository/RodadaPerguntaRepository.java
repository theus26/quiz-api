package com.mathe.quiz_biblico_api.repository;

import com.mathe.quiz_biblico_api.model.RodadaPergunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RodadaPerguntaRepository extends JpaRepository<RodadaPergunta, Long> {
    List<RodadaPergunta> findByRodadaId(Long rodadaId);

    boolean existsByRodadaIdAndPerguntaId(Long rodadaId, long perguntaId);
}
