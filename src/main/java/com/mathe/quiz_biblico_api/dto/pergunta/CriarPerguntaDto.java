package com.mathe.quiz_biblico_api.dto.pergunta;

import com.mathe.quiz_biblico_api.Enum.Nivel;
import com.mathe.quiz_biblico_api.Enum.Testamento;
import com.mathe.quiz_biblico_api.dto.alternativa.AlternativaCorretaDto;
import com.mathe.quiz_biblico_api.model.Pergunta;

public record CriarPerguntaDto(
        String enunciado,
        Testamento testamento,
        Nivel nivel,
        String referencia,
        AlternativaCorretaDto[] alternativas
) {
    public Pergunta toEntity() {
        return new Pergunta(enunciado, testamento, nivel, referencia);
    }
}
