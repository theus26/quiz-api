package com.mathe.quiz_biblico_api.Exception;

public class UsuarioNotFoundException extends NotFoundException {
    public UsuarioNotFoundException() {
        super("Usuário não encontrado!");
    }
}
