package com.mathe.quiz_biblico_api.dto.pontuacao;

import java.util.List;

public record RankingPessoalDto(
        boolean elegivel,
        Integer posicao,
        int pontos,
        int partidas,
        Integer faltamParaProximo,
        String nomeAcima,
        Integer evolucao,
        int recorde,
        int melhorAcertos,
        int sequenciaAtual,
        double taxaAcerto,
        List<String> conquistas,
        String mensagem
) {
}
