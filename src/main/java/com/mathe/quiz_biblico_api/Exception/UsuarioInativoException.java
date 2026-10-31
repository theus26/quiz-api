package com.mathe.quiz_biblico_api.Exception;

public class UsuarioInativoException extends RuntimeException {
    public UsuarioInativoException() {
        super("Usuário inativo.");
    }
}
