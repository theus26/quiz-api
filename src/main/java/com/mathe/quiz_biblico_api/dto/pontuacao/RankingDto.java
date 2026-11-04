package com.mathe.quiz_biblico_api.dto.pontuacao;

public record RankingDto(int posicao, long usuarioId, String nome, int pontos) {
}
