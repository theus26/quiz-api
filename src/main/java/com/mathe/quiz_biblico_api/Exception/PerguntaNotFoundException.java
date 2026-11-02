package com.mathe.quiz_biblico_api.Exception;

public class PerguntaNotFoundException extends NotFoundException {
    public PerguntaNotFoundException() {
        super("Pergunta não encontrada.");
    }
}
