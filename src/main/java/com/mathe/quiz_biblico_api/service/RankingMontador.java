package com.mathe.quiz_biblico_api.service;

import com.mathe.quiz_biblico_api.Enum.ResultadoPartida;
import com.mathe.quiz_biblico_api.dto.pontuacao.RankingItemDto;
import com.mathe.quiz_biblico_api.dto.pontuacao.RankingPainelDto;
import com.mathe.quiz_biblico_api.dto.pontuacao.RankingPessoalDto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class RankingMontador {
    private RankingMontador() {
    }

    public record Linha(
            long usuarioId,
            String nome,
            long pontos,
            long partidas,
            long acertos,
            long respondidas,
            long melhor
    ) {
    }

    public record Partida(int pontos, int acertos, int respondidas, ResultadoPartida resultado) {
    }

    public record Pedido(Long usuarioId, boolean administrador, List<Partida> partidasDaMaisRecente) {
    }

    public static RankingPainelDto montar(List<Linha> linhas, Pedido pedido) {
        List<Linha> ordenadas = ordenar(linhas);
        List<RankingItemDto> classificacao = new ArrayList<>();
        for (int i = 0; i < ordenadas.size(); i++) {
            Linha linha = ordenadas.get(i);
            classificacao.add(new RankingItemDto(
                    i + 1,
                    linha.usuarioId(),
                    linha.nome(),
                    Math.toIntExact(linha.pontos()),
                    Math.toIntExact(linha.partidas()),
                    Math.toIntExact(linha.acertos()),
                    Math.toIntExact(linha.respondidas()),
                    taxa(linha.acertos(), linha.respondidas()),
                    Math.toIntExact(linha.melhor())
            ));
        }
        if (pedido == null || pedido.usuarioId() == null) {
            return new RankingPainelDto(classificacao, null);
        }
        return new RankingPainelDto(classificacao, pessoal(ordenadas, pedido));
    }

    private static RankingPessoalDto pessoal(List<Linha> ordenadas, Pedido pedido) {
        List<Partida> partidas = pedido.partidasDaMaisRecente() == null
                ? List.of()
                : pedido.partidasDaMaisRecente();
        int pontos = partidas.stream().mapToInt(Partida::pontos).sum();
        int partidasCount = partidas.size();
        int acertos = partidas.stream().mapToInt(Partida::acertos).sum();
        int respondidas = partidas.stream().mapToInt(Partida::respondidas).sum();
        int recorde = partidas.stream().mapToInt(Partida::pontos).max().orElse(0);
        int melhorAcertos = partidas.stream().mapToInt(Partida::acertos).max().orElse(0);
        int sequencia = sequenciaAtual(partidas);
        double taxaAcerto = taxa(acertos, respondidas);

        if (pedido.administrador()) {
            return new RankingPessoalDto(
                    false,
                    null,
                    pontos,
                    partidasCount,
                    null,
                    null,
                    null,
                    recorde,
                    melhorAcertos,
                    sequencia,
                    taxaAcerto,
                    conquistas(null, pontos, partidasCount, respondidas, taxaAcerto, sequencia),
                    "Administradores acompanham o ranking, mas não entram na classificação."
            );
        }

        Integer posicao = posicaoDe(ordenadas, pedido.usuarioId());
        if (posicao == null || partidasCount == 0) {
            return new RankingPessoalDto(
                    true,
                    null,
                    pontos,
                    partidasCount,
                    null,
                    null,
                    null,
                    recorde,
                    melhorAcertos,
                    sequencia,
                    taxaAcerto,
                    conquistas(null, pontos, partidasCount, respondidas, taxaAcerto, sequencia),
                    "Jogue uma partida para entrar no ranking."
            );
        }

        Integer anterior = posicaoAnterior(ordenadas, pedido);
        Integer evolucao = anterior == null ? null : anterior - posicao;
        RankingItemDto acima = posicao > 1 ? item(ordenar(ordenadas), posicao - 2) : null;
        Integer faltam = acima == null ? 0 : Math.max(0, acima.pontos() - pontos);
        String nomeAcima = acima == null ? null : acima.nome();
        return new RankingPessoalDto(
                true,
                posicao,
                pontos,
                partidasCount,
                faltam,
                nomeAcima,
                evolucao,
                recorde,
                melhorAcertos,
                sequencia,
                taxaAcerto,
                conquistas(posicao, pontos, partidasCount, respondidas, taxaAcerto, sequencia),
                mensagem(posicao, faltam, nomeAcima, evolucao, partidasCount)
        );
    }

    private static RankingItemDto item(List<Linha> ordenadas, int indice) {
        Linha linha = ordenadas.get(indice);
        return new RankingItemDto(
                indice + 1,
                linha.usuarioId(),
                linha.nome(),
                Math.toIntExact(linha.pontos()),
                Math.toIntExact(linha.partidas()),
                Math.toIntExact(linha.acertos()),
                Math.toIntExact(linha.respondidas()),
                taxa(linha.acertos(), linha.respondidas()),
                Math.toIntExact(linha.melhor())
        );
    }

    private static Integer posicaoAnterior(List<Linha> ordenadas, Pedido pedido) {
        if (pedido.partidasDaMaisRecente() == null || pedido.partidasDaMaisRecente().isEmpty()) {
            return null;
        }
        Partida ultima = pedido.partidasDaMaisRecente().get(0);
        List<Linha> antes = new ArrayList<>();
        for (Linha linha : ordenadas) {
            if (linha.usuarioId() != pedido.usuarioId()) {
                antes.add(linha);
                continue;
            }
            long pontos = linha.pontos() - ultima.pontos();
            long partidas = linha.partidas() - 1;
            if (partidas <= 0) {
                continue;
            }
            antes.add(new Linha(
                    linha.usuarioId(),
                    linha.nome(),
                    Math.max(0, pontos),
                    partidas,
                    linha.acertos(),
                    linha.respondidas(),
                    linha.melhor()
            ));
        }
        return posicaoDe(ordenar(antes), pedido.usuarioId());
    }

    private static Integer posicaoDe(List<Linha> ordenadas, long usuarioId) {
        for (int i = 0; i < ordenadas.size(); i++) {
            if (ordenadas.get(i).usuarioId() == usuarioId) {
                return i + 1;
            }
        }
        return null;
    }

    private static List<Linha> ordenar(List<Linha> linhas) {
        return linhas.stream()
                .sorted(Comparator.comparingLong(Linha::pontos).reversed()
                        .thenComparing(Comparator.comparingLong(Linha::partidas).reversed())
                        .thenComparing(Linha::nome, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    private static int sequenciaAtual(List<Partida> partidas) {
        int sequencia = 0;
        for (Partida partida : partidas) {
            if (partida.resultado() == ResultadoPartida.ERRO || partida.acertos() <= 0) {
                break;
            }
            sequencia++;
        }
        return sequencia;
    }

    private static List<String> conquistas(
            Integer posicao,
            int pontos,
            int partidas,
            int respondidas,
            double taxaAcerto,
            int sequencia) {
        List<String> conquistas = new ArrayList<>();
        if (partidas >= 1) {
            conquistas.add("Primeira partida");
        }
        if (partidas >= 5) {
            conquistas.add("Peregrino");
        }
        if (partidas >= 10) {
            conquistas.add("Constância");
        }
        if (pontos >= 100) {
            conquistas.add("Cem pontos");
        }
        if (pontos >= 500) {
            conquistas.add("Quinhentos pontos");
        }
        if (respondidas >= 20 && taxaAcerto >= 80) {
            conquistas.add("Mira firme");
        }
        if (sequencia >= 3) {
            conquistas.add("Sequência de " + sequencia);
        }
        if (posicao != null && posicao == 1) {
            conquistas.add("Topo do ranking");
        } else if (posicao != null && posicao <= 3) {
            conquistas.add("Pódio");
        } else if (posicao != null && posicao <= 10) {
            conquistas.add("Entre os dez");
        }
        return List.copyOf(conquistas);
    }

    private static String mensagem(
            int posicao,
            Integer faltam,
            String nomeAcima,
            Integer evolucao,
            int partidas) {
        if (posicao == 1) {
            return partidas == 1
                    ? "Você está no topo logo na primeira pontuação registrada."
                    : "Você está no topo. Continue jogando para manter a liderança.";
        }
        if (partidas == 1) {
            return "Esta é a sua primeira pontuação no ranking. Mais uma partida pode aproximar você do topo.";
        }
        if (faltam != null && faltam > 0 && nomeAcima != null) {
            return "Você está a apenas " + faltam + " pontos de " + nomeAcima + ".";
        }
        if (faltam != null && faltam == 0 && nomeAcima != null) {
            return "Você tem os mesmos pontos de " + nomeAcima + ". Mais uma partida pode mudar a ordem.";
        }
        if (posicao <= 3) {
            return "Continue assim! Você está entre os melhores jogadores.";
        }
        if (evolucao != null && evolucao > 0) {
            return evolucao == 1
                    ? "Você subiu 1 posição desde a última partida."
                    : "Você subiu " + evolucao + " posições desde a última partida.";
        }
        return "Mais uma partida pode aproximar você do topo.";
    }

    public static double taxa(long acertos, long respondidas) {
        if (respondidas <= 0) {
            return 0;
        }
        return Math.round(acertos * 1000.0 / respondidas) / 10.0;
    }
}
