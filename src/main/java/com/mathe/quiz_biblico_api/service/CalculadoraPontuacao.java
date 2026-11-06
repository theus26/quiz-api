package com.mathe.quiz_biblico_api.service;

import com.mathe.quiz_biblico_api.Enum.Nivel;
import com.mathe.quiz_biblico_api.Enum.ResultadoPartida;

import java.util.List;

public final class CalculadoraPontuacao {
    private CalculadoraPontuacao() {
    }

    public record TentativaAvaliada(boolean correta, Nivel nivel) {
    }

    public record Resultado(int acertos, int erros, int respondidas, int pontos, ResultadoPartida resultado) {
    }

    public static Resultado calcular(List<TentativaAvaliada> tentativas, ResultadoPartida informado) {
        int acertos = 0;
        int erros = 0;
        int pontos = 0;
        for (TentativaAvaliada tentativa : tentativas) {
            if (tentativa.correta()) {
                acertos++;
                pontos += PontosNivel.de(tentativa.nivel());
            } else {
                erros++;
            }
        }
        ResultadoPartida resultado = erros > 0
                ? ResultadoPartida.ERRO
                : resultadoSemErro(informado);
        return new Resultado(acertos, erros, tentativas.size(), pontos, resultado);
    }

    private static ResultadoPartida resultadoSemErro(ResultadoPartida informado) {
        if (informado == ResultadoPartida.VITORIA
                || informado == ResultadoPartida.PARADA
                || informado == ResultadoPartida.ESGOTOU) {
            return informado;
        }
        return ResultadoPartida.PARADA;
    }
}
