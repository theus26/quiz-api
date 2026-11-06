package com.mathe.quiz_biblico_api.repository;

import com.mathe.quiz_biblico_api.model.Rodada;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RodadaRepository extends JpaRepository<Rodada, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Rodada r WHERE r.token = :token")
    Optional<Rodada> findByTokenParaAtualizar(@Param("token") String token);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Rodada r SET r.usuario = null WHERE r.usuario.id = :usuarioId")
    int desvincularUsuario(@Param("usuarioId") long usuarioId);
}
