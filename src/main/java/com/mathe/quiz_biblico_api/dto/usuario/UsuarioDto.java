package com.mathe.quiz_biblico_api.dto.usuario;

import com.mathe.quiz_biblico_api.Enum.Role;
import com.mathe.quiz_biblico_api.model.Usuario;

import java.time.Instant;

public record UsuarioDto(long id, String nome, String email, boolean ativo, Role role, Instant criadoEm) {
    public static UsuarioDto fromEntity(Usuario usuario) {
        return new UsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getAtivo(),
                usuario.getRole(),
                usuario.getCriadoEm()
        );
    }
}
