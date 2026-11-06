package com.mathe.quiz_biblico_api.repository;

import com.mathe.quiz_biblico_api.Enum.Role;
import com.mathe.quiz_biblico_api.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Page<Usuario> findByAtivo(boolean ativo, Pageable pageable);

    Optional<Usuario> findByEmailIgnoreCase(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    long countByRole(Role role);

    long countByRoleAndAtivo(Role role, boolean ativo);

    @Query("""
            SELECT u FROM Usuario u
            WHERE (:ativo IS NULL OR u.ativo = :ativo)
              AND (
                :busca IS NULL OR :busca = ''
                OR LOWER(u.nome) LIKE LOWER(CONCAT('%', :busca, '%'))
                OR LOWER(u.email) LIKE LOWER(CONCAT('%', :busca, '%'))
              )
            """)
    Page<Usuario> pesquisar(@Param("ativo") Boolean ativo, @Param("busca") String busca, Pageable pageable);
}
