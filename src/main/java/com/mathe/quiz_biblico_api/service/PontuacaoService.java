package com.mathe.quiz_biblico_api.service;

import com.mathe.quiz_biblico_api.Exception.PerguntaNotFoundException;
import com.mathe.quiz_biblico_api.Exception.UsuarioNotFoundException;
import com.mathe.quiz_biblico_api.dto.pontuacao.CriarPontuacaoDto;
import com.mathe.quiz_biblico_api.dto.pontuacao.PontuacaoDto;
import com.mathe.quiz_biblico_api.dto.pontuacao.RankingDto;
import com.mathe.quiz_biblico_api.model.Conjunto;
import com.mathe.quiz_biblico_api.model.Pergunta;
import com.mathe.quiz_biblico_api.model.Pontuacao;
import com.mathe.quiz_biblico_api.model.Usuario;
import com.mathe.quiz_biblico_api.repository.ConjuntoRepository;
import com.mathe.quiz_biblico_api.repository.PerguntaRepository;
import com.mathe.quiz_biblico_api.repository.PontuacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class PontuacaoService {
    private final PontuacaoRepository _pontuacaoRepository;
    private final ConjuntoRepository _conjuntoRepository;
    private final PerguntaRepository _perguntaRepository;
    private final UsuarioAutenticadoService _usuarioAutenticadoService;

    public PontuacaoService(
            PontuacaoRepository pontuacaoRepository,
            ConjuntoRepository conjuntoRepository,
            PerguntaRepository perguntaRepository,
            UsuarioAutenticadoService usuarioAutenticadoService) {
        this._pontuacaoRepository = pontuacaoRepository;
        this._conjuntoRepository = conjuntoRepository;
        this._perguntaRepository = perguntaRepository;
        this._usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @Transactional(rollbackFor = Exception.class)
    public PontuacaoDto registrar(CriarPontuacaoDto dto) throws UsuarioNotFoundException, PerguntaNotFoundException {
        validar(dto);
        Usuario usuario = _usuarioAutenticadoService.atual();
        List<Pergunta> perguntas = buscarPerguntas(dto.perguntaIds());

        Pontuacao pontuacao = new Pontuacao();
        pontuacao.setUsuario(usuario);
        pontuacao.setFiltroTestamento(dto.filtroTestamento());
        pontuacao.setAcertos(dto.acertos());
        pontuacao.setPontos(dto.pontos());
        pontuacao.setFinalizadaEm(Instant.now());

        Pontuacao salva = _pontuacaoRepository.save(pontuacao);
        salvarConjunto(usuario, perguntas);
        return PontuacaoDto.fromEntity(salva);
    }

    @Transactional(readOnly = true)
    public List<RankingDto> ranking() {
        List<RankingDto> ranking = new ArrayList<>();
        int posicao = 1;
        for (Object[] linha : _pontuacaoRepository.ranking()) {
            long usuarioId = ((Number) linha[0]).longValue();
            String nome = (String) linha[1];
            int pontos = ((Number) linha[2]).intValue();
            ranking.add(new RankingDto(posicao++, usuarioId, nome, pontos));
        }
        return ranking;
    }

    @Transactional(readOnly = true)
    public List<PontuacaoDto> minhas() throws UsuarioNotFoundException {
        long usuarioId = _usuarioAutenticadoService.atual().getId();
        return _pontuacaoRepository.findByUsuarioId(usuarioId).stream()
                .map(PontuacaoDto::fromEntity)
                .toList();
    }

    private void salvarConjunto(Usuario usuario, List<Pergunta> perguntas) {
        List<Long> perguntaIds = perguntas.stream().map(Pergunta::getId).toList();
        Set<Long> jaVistas = new HashSet<>(
                _conjuntoRepository.findPerguntaIdsByUsuarioIdAndPerguntaIdIn(usuario.getId(), perguntaIds)
        );

        List<Conjunto> novos = new ArrayList<>();
        for (Pergunta pergunta : perguntas) {
            if (jaVistas.contains(pergunta.getId())) {
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

    private List<Pergunta> buscarPerguntas(List<Long> perguntaIds) throws PerguntaNotFoundException {
        List<Long> ids = perguntaIds.stream().filter(Objects::nonNull).distinct().toList();
        List<Pergunta> perguntas = _perguntaRepository.findAllById(ids);
        if (perguntas.size() != ids.size()) {
            throw new PerguntaNotFoundException();
        }
        return perguntas;
    }

    private void validar(CriarPontuacaoDto dto) {
        if (dto.filtroTestamento() == null) {
            throw new IllegalArgumentException("O filtro de testamento é obrigatório.");
        }
        if (dto.acertos() < 0 || dto.pontos() < 0) {
            throw new IllegalArgumentException("Acertos e pontos não podem ser negativos.");
        }
        if (dto.perguntaIds() == null || dto.perguntaIds().isEmpty()
                || dto.perguntaIds().stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Informe as perguntas respondidas.");
        }
    }
}
