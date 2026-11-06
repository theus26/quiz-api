package com.mathe.quiz_biblico_api.service;

import com.mathe.quiz_biblico_api.Enum.Nivel;
import com.mathe.quiz_biblico_api.Enum.ResultadoPartida;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraPontuacaoTest {

    @Test
    void ignoraResultadoInformadoQuandoHaErroESomaSoOsAcertos() {
        var resultado = CalculadoraPontuacao.calcular(List.of(
                new CalculadoraPontuacao.TentativaAvaliada(true, Nivel.FACIL),
                new CalculadoraPontuacao.TentativaAvaliada(true, Nivel.MEDIO),
                new CalculadoraPontuacao.TentativaAvaliada(false, Nivel.DIFICIL)
        ), ResultadoPartida.VITORIA);

        assertEquals(2, resultado.acertos());
        assertEquals(1, resultado.erros());
        assertEquals(3, resultado.respondidas());
        assertEquals(15, resultado.pontos());
        assertEquals(ResultadoPartida.ERRO, resultado.resultado());
    }

    @Test
    void preservaParadaQuandoTodasAsRespostasEstaoCertas() {
        var resultado = CalculadoraPontuacao.calcular(List.of(
                new CalculadoraPontuacao.TentativaAvaliada(true, Nivel.DIFICIL)
        ), ResultadoPartida.PARADA);

        assertEquals(15, resultado.pontos());
        assertEquals(ResultadoPartida.PARADA, resultado.resultado());
    }
}
