package com.mathe.quiz_biblico_api.Exception;

public class AcessoNegadoException extends RuntimeException {
    public AcessoNegadoException() {
        super("Sem permissão.");
    }
}
