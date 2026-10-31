package com.mathe.quiz_biblico_api.dto.usuario;

import com.mathe.quiz_biblico_api.model.Usuario;

public record CriarUsuarioDto(String nome, String email, String senha){
    public Usuario toEntity(){
        return new Usuario(nome, email, senha);
    }
}

