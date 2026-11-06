package com.mathe.quiz_biblico_api.dto.pergunta;

import com.mathe.quiz_biblico_api.Enum.Nivel;
import com.mathe.quiz_biblico_api.Enum.StatusPergunta;
import com.mathe.quiz_biblico_api.Enum.Testamento;

import java.time.Instant;

public record PerguntaResumoDto(
        long id,
        String enunciado,
        Testamento testamento,
        Nivel nivel,
        String categoria,
        StatusPergunta status,
        String autorNome,
        Instant criadaEm
) {
}
