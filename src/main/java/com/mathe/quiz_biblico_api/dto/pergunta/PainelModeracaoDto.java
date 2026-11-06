package com.mathe.quiz_biblico_api.dto.pergunta;

import java.util.List;

public record PainelModeracaoDto(
        long pendentes,
        long aprovadas,
        long rejeitadas,
        List<PerguntaResumoDto> perguntas
) {
}
