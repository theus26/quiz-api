package com.mathe.quiz_biblico_api.service;

import com.mathe.quiz_biblico_api.Enum.FiltroTestamento;
import com.mathe.quiz_biblico_api.Enum.Modalidade;
import com.mathe.quiz_biblico_api.Enum.ResultadoPartida;
import com.mathe.quiz_biblico_api.Enum.Role;
import com.mathe.quiz_biblico_api.Exception.AcessoNegadoException;
import com.mathe.quiz_biblico_api.Exception.PerguntaNotFoundException;
import com.mathe.quiz_biblico_api.Exception.UsuarioNotFoundException;
import com.mathe.quiz_biblico_api.dto.pontuacao.CriarPontuacaoDto;
import com.mathe.quiz_biblico_api.dto.pontuacao.PontuacaoDto;
import com.mathe.quiz_biblico_api.dto.pontuacao.RankingPainelDto;
import com.mathe.quiz_biblico_api.model.Conjunto;
import com.mathe.quiz_biblico_api.model.Pergunta;
import com.mathe.quiz_biblico_api.model.Pontuacao;
import com.mathe.quiz_biblico_api.model.Usuario;
import com.mathe.quiz_biblico_api.repository.ConjuntoRepository;
import com.mathe.quiz_biblico_api.repository.PontuacaoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class PontuacaoService {
    private final PontuacaoRepository _pontuacaoRepository;
    private final ConjuntoRepository _conjuntoRepository;
    private final UsuarioAutenticadoService _usuarioAutenticadoService;
    private final RodadaService _rodadaService;

    public PontuacaoService(
            PontuacaoRepository pontuacaoRepository,
            ConjuntoRepository conjuntoRepository,
            UsuarioAutenticadoService usuarioAutenticadoService,
            RodadaService rodadaService) {
        this._pontuacaoRepository = pontuacaoRepository;
        this._conjuntoRepository = conjuntoRepository;
        this._usuarioAutenticadoService = usuarioAutenticadoService;
        this._rodadaService = rodadaService;
    }

    @Transactional(rollbackFor = Exception.class)
    public PontuacaoDto registrar(CriarPontuacaoDto dto) throws UsuarioNotFoundException, PerguntaNotFoundException {
        if (dto == null || dto.rodadaToken() == null || dto.rodadaToken().isBlank()) {
            throw new IllegalArgumentException("A partida não foi informada.");
        }
        Usuario usuario = _usuarioAutenticadoService.atual();
        RodadaService.Encerramento encerramento = _rodadaService.encerrar(dto.rodadaToken(), dto.resultado(), usuario);
        CalculadoraPontuacao.Resultado calculo = encerramento.calculo();

        Pontuacao pontuacao = new Pontuacao();
        pontuacao.setUsuario(usuario);
        pontuacao.setFiltroTestamento(encerramento.rodada().getFiltroTestamento());
        pontuacao.setModalidade(encerramento.rodada().getModalidade());
        pontuacao.setNivel(encerramento.rodada().getNivel());
        pontuacao.setAcertos(calculo.acertos());
        pontuacao.setErros(calculo.erros());
        pontuacao.setRespondidas(calculo.respondidas());
        pontuacao.setPontos(calculo.pontos());
        pontuacao.setResultado(calculo.resultado());
        pontuacao.setFinalizadaEm(Instant.now());

        Pontuacao salva = _pontuacaoRepository.save(pontuacao);
        salvarConjunto(usuario, encerramento.perguntas());
        return PontuacaoDto.fromEntity(salva);
    }

    @Transactional(readOnly = true)
    public RankingPainelDto ranking() {
        List<RankingMontador.Linha> linhas = new ArrayList<>();
        for (Object[] linha : _pontuacaoRepository.ranking(Role.ADMIN)) {
            linhas.add(new RankingMontador.Linha(
                    ((Number) linha[0]).longValue(),
                    (String) linha[1],
                    ((Number) linha[2]).longValue(),
                    ((Number) linha[3]).longValue(),
                    ((Number) linha[4]).longValue(),
                    ((Number) linha[5]).longValue(),
                    ((Number) linha[6]).longValue()
            ));
        }
        Usuario atual = _usuarioAutenticadoService.atualOpcional().filter(Usuario::getAtivo).orElse(null);
        if (atual == null) {
            return RankingMontador.montar(linhas, null);
        }
        List<RankingMontador.Partida> partidas = _pontuacaoRepository.findByUsuarioId(atual.getId()).stream()
                .map(this::paraResumo)
                .toList();
        return RankingMontador.montar(
                linhas,
                new RankingMontador.Pedido(atual.getId(), atual.getRole() == Role.ADMIN, partidas)
        );
    }

    @Transactional(readOnly = true)
    public List<PontuacaoDto> minhas(FiltroTestamento filtro, Modalidade modalidade, ResultadoPartida resultado)
            throws UsuarioNotFoundException {
        long usuarioId = _usuarioAutenticadoService.atual().getId();
        return filtrar(_pontuacaoRepository.findByUsuarioId(usuarioId), filtro, modalidade, resultado, null);
    }

    @Transactional(readOnly = true)
    public List<PontuacaoDto> historico(
            FiltroTestamento filtro,
            Modalidade modalidade,
            ResultadoPartida resultado,
            String busca) {
        if (!_usuarioAutenticadoService.adminNoBanco()) {
            throw new AcessoNegadoException();
        }
        return filtrar(
                _pontuacaoRepository.historicoRecente(PageRequest.of(0, 400)),
                filtro,
                modalidade,
                resultado,
                busca
        );
    }

    private RankingMontador.Partida paraResumo(Pontuacao pontuacao) {
        int respondidas = pontuacao.getRespondidas() == null ? pontuacao.getAcertos() : pontuacao.getRespondidas();
        int erros = pontuacao.getErros() == null ? Math.max(0, respondidas - pontuacao.getAcertos()) : pontuacao.getErros();
        ResultadoPartida resultado = pontuacao.getResultado();
        if (resultado == null) {
            resultado = erros > 0 ? ResultadoPartida.ERRO : ResultadoPartida.PARADA;
        }
        return new RankingMontador.Partida(pontuacao.getPontos(), pontuacao.getAcertos(), respondidas, resultado);
    }

    private List<PontuacaoDto> filtrar(
            List<Pontuacao> partidas,
            FiltroTestamento filtro,
            Modalidade modalidade,
            ResultadoPartida resultado,
            String busca) {
        String termo = busca == null ? "" : busca.trim().toLowerCase(Locale.ROOT);
        return partidas.stream()
                .filter(partida -> filtro == null || partida.getFiltroTestamento() == filtro)
                .filter(partida -> modalidade == null || modalidadeDe(partida) == modalidade)
                .filter(partida -> resultado == null || resultadoDe(partida) == resultado)
                .filter(partida -> termo.isEmpty() || partida.getUsuario().getNome().toLowerCase(Locale.ROOT).contains(termo))
                .map(PontuacaoDto::fromEntity)
                .toList();
    }

    private Modalidade modalidadeDe(Pontuacao pontuacao) {
        return pontuacao.getModalidade() == null ? Modalidade.CLASSICO : pontuacao.getModalidade();
    }

    private ResultadoPartida resultadoDe(Pontuacao pontuacao) {
        if (pontuacao.getResultado() != null) {
            return pontuacao.getResultado();
        }
        int respondidas = pontuacao.getRespondidas() == null ? pontuacao.getAcertos() : pontuacao.getRespondidas();
        int erros = pontuacao.getErros() == null ? Math.max(0, respondidas - pontuacao.getAcertos()) : pontuacao.getErros();
        return erros > 0 ? ResultadoPartida.ERRO : ResultadoPartida.PARADA;
    }

    private void salvarConjunto(Usuario usuario, List<Pergunta> perguntas) {
        List<Long> perguntaIds = perguntas.stream().map(Pergunta::getId).distinct().toList();
        Set<Long> jaVistas = new HashSet<>(
                _conjuntoRepository.findPerguntaIdsByUsuarioIdAndPerguntaIdIn(usuario.getId(), perguntaIds)
        );
        List<Conjunto> novos = new ArrayList<>();
        for (Pergunta pergunta : perguntas) {
            if (!jaVistas.add(pergunta.getId())) {
                continue;
            }
            Conjunto conjunto = new Conjunto();
            conjunto.setUsuario(usuario);
            conjunto.setPergunta(pergunta);
            novos.add(conjunto);
        }
        if (!novos.isEmpty()) {
            _conjuntoRepository.saveAll(novos);
        }
    }
}
