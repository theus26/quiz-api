package com.mathe.quiz_biblico_api.dto.alternativa;

import com.mathe.quiz_biblico_api.model.Alternativa;
import com.mathe.quiz_biblico_api.model.Pergunta;

public record AlternativaCorretaDto(String texto, boolean correta) {
    public Alternativa toEntity(Pergunta pergunta, short ordem) {
        return new Alternativa(pergunta, texto, correta, ordem);
    }
}
