package com.mathe.quiz_biblico_api.dto.pontuacao;

public record RankingItemDto(
        int posicao,
        long usuarioId,
        String nome,
        int pontos,
        int partidas,
        int acertos,
        int respondidas,
        double taxaAcerto,
        int melhorPontuacao
) {
}
