package com.mathe.quiz_biblico_api.dto.pontuacao;

import com.mathe.quiz_biblico_api.Enum.FiltroTestamento;
import com.mathe.quiz_biblico_api.model.Pontuacao;

import java.time.Instant;

public record PontuacaoDto(
        long id,
        long usuarioId,
        String nome,
        FiltroTestamento filtroTestamento,
        int acertos,
        int pontos,
        Instant finalizadaEm
) {
    public static PontuacaoDto fromEntity(Pontuacao pontuacao) {
        return new PontuacaoDto(
                pontuacao.getId(),
                pontuacao.getUsuario().getId(),
                pontuacao.getUsuario().getNome(),
                pontuacao.getFiltroTestamento(),
                pontuacao.getAcertos(),
                pontuacao.getPontos(),
                pontuacao.getFinalizadaEm()
        );
    }
}
