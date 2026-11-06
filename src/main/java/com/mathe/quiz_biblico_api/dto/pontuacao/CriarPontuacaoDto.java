package com.mathe.quiz_biblico_api.dto.pontuacao;

import com.mathe.quiz_biblico_api.Enum.ResultadoPartida;

public record CriarPontuacaoDto(String rodadaToken, ResultadoPartida resultado) {
}
