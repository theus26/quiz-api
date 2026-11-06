package com.mathe.quiz_biblico_api.dto.pergunta;

import com.mathe.quiz_biblico_api.Enum.Nivel;
import com.mathe.quiz_biblico_api.Enum.StatusPergunta;
import com.mathe.quiz_biblico_api.Enum.Testamento;
import com.mathe.quiz_biblico_api.dto.alternativa.AlternativaAdminDto;

import java.time.Instant;
import java.util.List;

public record PerguntaDetalheDto(
        long id,
        String enunciado,
        Testamento testamento,
        Nivel nivel,
        String referencia,
        String categoria,
        String explicacao,
        StatusPergunta status,
        String motivoRejeicao,
        String autorNome,
        String revisorNome,
        Instant criadaEm,
        Instant revisadaEm,
        boolean propria,
        List<AlternativaAdminDto> alternativas
) {
}
