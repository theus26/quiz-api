package com.mathe.quiz_biblico_api.dto.usuario;

import com.mathe.quiz_biblico_api.model.Role;
import com.mathe.quiz_biblico_api.model.Usuario;

public record UsuarioDto(long id, String nome, String email, boolean ativo, Role role) {
    public static UsuarioDto fromEntity(Usuario usuario) {
        return new UsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getAtivo(),
                usuario.getRole()
        );
    }
}
