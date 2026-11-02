package com.mathe.quiz_biblico_api.dto.pergunta;

import com.mathe.quiz_biblico_api.Enum.Nivel;
import com.mathe.quiz_biblico_api.Enum.Testamento;
import com.mathe.quiz_biblico_api.model.Pergunta;

public record PerguntaDto(long id, String enunciado, Testamento testamento, Nivel nivel, String referencia) {
    public static PerguntaDto fromEntity(Pergunta pergunta) {
        return new PerguntaDto(pergunta.getId(), pergunta.getEnunciado(), pergunta.getTestamento(), pergunta.getNivel(), pergunta.getReferencia());
    }
}
