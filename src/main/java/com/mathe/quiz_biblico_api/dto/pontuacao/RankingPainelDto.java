package com.mathe.quiz_biblico_api.dto.pontuacao;

import java.util.List;

public record RankingPainelDto(List<RankingItemDto> classificacao, RankingPessoalDto pessoal) {
}
