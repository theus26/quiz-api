package com.mathe.quiz_biblico_api.dto.partida;

import com.mathe.quiz_biblico_api.Enum.FiltroTestamento;
import com.mathe.quiz_biblico_api.Enum.Modalidade;
import com.mathe.quiz_biblico_api.Enum.Nivel;
import com.mathe.quiz_biblico_api.dto.pergunta.PerguntaComAlternativasDto;

import java.util.List;

public record PartidaAbertaDto(
        String token,
        FiltroTestamento filtroTestamento,
        Modalidade modalidade,
        Nivel nivel,
        List<PerguntaComAlternativasDto> perguntas
) {
}
