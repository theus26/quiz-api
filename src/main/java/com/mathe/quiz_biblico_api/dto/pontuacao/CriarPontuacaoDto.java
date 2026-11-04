package com.mathe.quiz_biblico_api.dto.pontuacao;

import com.mathe.quiz_biblico_api.Enum.FiltroTestamento;

import java.util.List;

public record CriarPontuacaoDto(
        FiltroTestamento filtroTestamento,
        int acertos,
        int pontos,
        List<Long> perguntaIds
) {
}
