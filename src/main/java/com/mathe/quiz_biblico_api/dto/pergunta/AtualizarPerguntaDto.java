package com.mathe.quiz_biblico_api.dto.pergunta;

import com.mathe.quiz_biblico_api.Enum.Nivel;
import com.mathe.quiz_biblico_api.Enum.Testamento;
import com.mathe.quiz_biblico_api.dto.alternativa.AlternativaCorretaDto;

public record AtualizarPerguntaDto(
        String enunciado,
        Testamento testamento,
        Nivel nivel,
        String referencia,
        AlternativaCorretaDto[] alternativas
) {
}
