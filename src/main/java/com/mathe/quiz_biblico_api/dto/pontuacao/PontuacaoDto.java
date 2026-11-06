package com.mathe.quiz_biblico_api.dto.pontuacao;

import com.mathe.quiz_biblico_api.Enum.FiltroTestamento;
import com.mathe.quiz_biblico_api.Enum.Modalidade;
import com.mathe.quiz_biblico_api.Enum.Nivel;
import com.mathe.quiz_biblico_api.Enum.ResultadoPartida;
import com.mathe.quiz_biblico_api.model.Pontuacao;

import java.time.Instant;

public record PontuacaoDto(
        long id,
        long usuarioId,
        String nome,
        FiltroTestamento filtroTestamento,
        Modalidade modalidade,
        Nivel nivel,
        int acertos,
        int erros,
        int respondidas,
        int pontos,
        ResultadoPartida resultado,
        Instant finalizadaEm
) {
    public static PontuacaoDto fromEntity(Pontuacao pontuacao) {
        int acertos = pontuacao.getAcertos();
        int respondidas = pontuacao.getRespondidas() == null ? acertos : pontuacao.getRespondidas();
        int erros = pontuacao.getErros() == null ? Math.max(0, respondidas - acertos) : pontuacao.getErros();
        ResultadoPartida resultado = pontuacao.getResultado();
        if (resultado == null) {
            resultado = erros > 0 ? ResultadoPartida.ERRO : ResultadoPartida.PARADA;
        }
        return new PontuacaoDto(
                pontuacao.getId(),
                pontuacao.getUsuario().getId(),
                pontuacao.getUsuario().getNome(),
                pontuacao.getFiltroTestamento(),
                pontuacao.getModalidade() == null ? Modalidade.CLASSICO : pontuacao.getModalidade(),
                pontuacao.getNivel(),
                acertos,
                erros,
                respondidas,
                pontuacao.getPontos(),
                resultado,
                pontuacao.getFinalizadaEm()
        );
    }
}
