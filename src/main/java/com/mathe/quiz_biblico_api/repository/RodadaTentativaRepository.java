package com.mathe.quiz_biblico_api.repository;

import com.mathe.quiz_biblico_api.model.RodadaTentativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RodadaTentativaRepository extends JpaRepository<RodadaTentativa, Long> {
    Optional<RodadaTentativa> findByRodadaIdAndPerguntaId(Long rodadaId, long perguntaId);

    List<RodadaTentativa> findByRodadaId(Long rodadaId);
}
