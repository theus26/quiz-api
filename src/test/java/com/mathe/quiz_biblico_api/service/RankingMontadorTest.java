package com.mathe.quiz_biblico_api.service;

import com.mathe.quiz_biblico_api.Enum.ResultadoPartida;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RankingMontadorTest {

    @Test
    void administradorNaoRecebePosicaoMesmoComPontos() {
        var linhas = List.of(new RankingMontador.Linha(2, "Ana", 80, 2, 10, 12, 50));
        var pedido = new RankingMontador.Pedido(
                1L,
                true,
                List.of(new RankingMontador.Partida(40, 4, 5, ResultadoPartida.PARADA))
        );

        var painel = RankingMontador.montar(linhas, pedido);

        assertEquals(1, painel.classificacao().size());
        assertEquals("Ana", painel.classificacao().get(0).nome());
        assertFalse(painel.pessoal().elegivel());
        assertNull(painel.pessoal().posicao());
        assertEquals(40, painel.pessoal().pontos());
        assertTrue(painel.pessoal().mensagem().contains("não entram"));
    }

    @Test
    void calculaPontosQueFaltamParaOJogadorDeCima() {
        var linhas = List.of(
                new RankingMontador.Linha(1, "Ana", 80, 3, 20, 24, 40),
                new RankingMontador.Linha(2, "Beto", 30, 2, 6, 10, 20)
        );
        var pedido = new RankingMontador.Pedido(
                2L,
                false,
                List.of(
                        new RankingMontador.Partida(10, 2, 4, ResultadoPartida.PARADA),
                        new RankingMontador.Partida(20, 4, 6, ResultadoPartida.VITORIA)
                )
        );

        var pessoal = RankingMontador.montar(linhas, pedido).pessoal();

        assertEquals(2, pessoal.posicao());
        assertEquals(30, pessoal.pontos());
        assertEquals(50, pessoal.faltamParaProximo());
        assertEquals("Ana", pessoal.nomeAcima());
        assertTrue(pessoal.mensagem().contains("50 pontos"));
    }

    @Test
    void medeSubidaRealDepoisDaUltimaPartida() {
        var linhas = List.of(
                new RankingMontador.Linha(2, "Beto", 50, 2, 8, 10, 40),
                new RankingMontador.Linha(1, "Ana", 30, 2, 6, 8, 20)
        );
        var pedido = new RankingMontador.Pedido(
                2L,
                false,
                List.of(new RankingMontador.Partida(40, 4, 4, ResultadoPartida.VITORIA),
                        new RankingMontador.Partida(10, 2, 4, ResultadoPartida.PARADA))
        );

        var pessoal = RankingMontador.montar(linhas, pedido).pessoal();

        assertEquals(1, pessoal.posicao());
        assertEquals(1, pessoal.evolucao());
    }
}
