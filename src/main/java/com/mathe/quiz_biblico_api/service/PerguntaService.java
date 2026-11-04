package com.mathe.quiz_biblico_api.service;

import com.mathe.quiz_biblico_api.Enum.FiltroTestamento;
import com.mathe.quiz_biblico_api.Enum.Nivel;
import com.mathe.quiz_biblico_api.Enum.Testamento;
import com.mathe.quiz_biblico_api.Exception.PerguntaNotFoundException;
import com.mathe.quiz_biblico_api.Exception.UsuarioNotFoundException;
import com.mathe.quiz_biblico_api.dto.PaginacaoDto;
import com.mathe.quiz_biblico_api.dto.alternativa.AlternativaAdminDto;
import com.mathe.quiz_biblico_api.dto.alternativa.AlternativaDto;
import com.mathe.quiz_biblico_api.dto.pergunta.AtualizarPerguntaDto;
import com.mathe.quiz_biblico_api.dto.pergunta.CriarPerguntaDto;
import com.mathe.quiz_biblico_api.dto.pergunta.PerguntaAdminDto;
import com.mathe.quiz_biblico_api.dto.pergunta.PerguntaComAlternativasDto;
import com.mathe.quiz_biblico_api.model.Pergunta;
import com.mathe.quiz_biblico_api.repository.ConjuntoRepository;
import com.mathe.quiz_biblico_api.repository.PerguntaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class PerguntaService {
    private static final int QUANTIDADE_DIFICIL = 5;
    private static final int QUANTIDADE_MEDIO = 5;
    private static final int QUANTIDADE_FACIL = 7;
    private static final int TAMANHO_MAXIMO_PAGINA = 100;

    private final PerguntaRepository _perguntaRepository;
    private final AlternativaService _alternativaService;
    private final ConjuntoRepository _conjuntoRepository;
    private final UsuarioAutenticadoService _usuarioAutenticadoService;

    public PerguntaService(
            PerguntaRepository perguntaRepository,
            AlternativaService alternativaService,
            ConjuntoRepository conjuntoRepository,
            UsuarioAutenticadoService usuarioAutenticadoService) {
        this._perguntaRepository = perguntaRepository;
        this._alternativaService = alternativaService;
        this._conjuntoRepository = conjuntoRepository;
        this._usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @Transactional(rollbackFor = Exception.class)
    public PerguntaAdminDto criar(CriarPerguntaDto dto) {
        validarPergunta(dto.enunciado(), dto.testamento(), dto.nivel());
        _alternativaService.validar(dto.alternativas());

        Pergunta pergunta = _perguntaRepository.save(dto.toEntity());
        _alternativaService.criar(pergunta, dto.alternativas());
        return paraGestao(pergunta);
    }

    @Transactional(rollbackFor = Exception.class)
    public PerguntaAdminDto atualizar(Long id, AtualizarPerguntaDto dto) throws PerguntaNotFoundException {
        validarPergunta(dto.enunciado(), dto.testamento(), dto.nivel());
        Pergunta pergunta = _perguntaRepository.findById(id).orElseThrow(PerguntaNotFoundException::new);
        pergunta.setEnunciado(dto.enunciado());
        pergunta.setTestamento(dto.testamento());
        pergunta.setNivel(dto.nivel());
        pergunta.setReferencia(dto.referencia());
        if (dto.alternativas() != null) {
            _alternativaService.substituir(pergunta, dto.alternativas());
        }

        return paraGestao(_perguntaRepository.save(pergunta));
    }

    @Transactional(readOnly = true)
    public PerguntaComAlternativasDto obter(Long id) throws PerguntaNotFoundException {
        Pergunta pergunta = _perguntaRepository.findById(id).orElseThrow(PerguntaNotFoundException::new);
        return PerguntaComAlternativasDto.fromEntity(pergunta, _alternativaService.listarPorPergunta(id));
    }

    @Transactional(readOnly = true)
    public List<PerguntaComAlternativasDto> listar(FiltroTestamento filtro) throws UsuarioNotFoundException {
        Testamento testamento = filtro == null || filtro == FiltroTestamento.AMBOS
                ? null
                : Testamento.valueOf(filtro.name());
        Collection<Long> excluirIds = perguntasJaVistas(_usuarioAutenticadoService.atual().getId());

        List<Pergunta> perguntas = new ArrayList<>();
        perguntas.addAll(sortear(Nivel.DIFICIL, QUANTIDADE_DIFICIL, testamento, excluirIds));
        perguntas.addAll(sortear(Nivel.MEDIO, QUANTIDADE_MEDIO, testamento, excluirIds));
        perguntas.addAll(sortear(Nivel.FACIL, QUANTIDADE_FACIL, testamento, excluirIds));
        Collections.shuffle(perguntas);

        List<Long> ids = perguntas.stream().map(Pergunta::getId).toList();
        Map<Long, List<AlternativaDto>> alternativas = _alternativaService.listarPorPerguntas(ids);

        return perguntas.stream()
                .map(pergunta -> PerguntaComAlternativasDto.fromEntity(
                        pergunta,
                        alternativas.getOrDefault(pergunta.getId(), List.of())
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean conferir(Long perguntaId, Long alternativaId) {
        return _alternativaService.conferir(perguntaId, alternativaId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletar(Long id) throws PerguntaNotFoundException {
        if (!_perguntaRepository.existsById(id)) {
            throw new PerguntaNotFoundException();
        }
        _conjuntoRepository.deleteByPerguntaId(id);
        _alternativaService.deletarPorPergunta(id);
        _perguntaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PaginacaoDto<PerguntaAdminDto> gerenciar(int pagina, int tamanho) {
        if (pagina < 1) {
            throw new IllegalArgumentException("A página deve ser maior ou igual a 1.");
        }
        if (tamanho < 1 || tamanho > TAMANHO_MAXIMO_PAGINA) {
            throw new IllegalArgumentException("O tamanho da página deve estar entre 1 e " + TAMANHO_MAXIMO_PAGINA + ".");
        }

        Pageable pageable = PageRequest.of(pagina - 1, tamanho, Sort.by(Sort.Order.asc("id")));
        Page<Pergunta> resultado = _perguntaRepository.findAll(pageable);
        List<Long> ids = resultado.getContent().stream().map(Pergunta::getId).toList();
        Map<Long, List<AlternativaAdminDto>> alternativas = _alternativaService.listarParaGestao(ids);

        List<PerguntaAdminDto> dados = resultado.getContent().stream()
                .map(pergunta -> PerguntaAdminDto.fromEntity(
                        pergunta,
                        alternativas.getOrDefault(pergunta.getId(), List.of())
                ))
                .toList();

        return new PaginacaoDto<>(dados, Math.toIntExact(resultado.getTotalElements()), resultado.getTotalPages());
    }

    private PerguntaAdminDto paraGestao(Pergunta pergunta) {
        List<AlternativaAdminDto> alternativas = _alternativaService
                .listarParaGestao(List.of(pergunta.getId()))
                .getOrDefault(pergunta.getId(), List.of());
        return PerguntaAdminDto.fromEntity(pergunta, alternativas);
    }

    private List<Pergunta> sortear(
            Nivel nivel,
            int quantidade,
            Testamento testamento,
            Collection<Long> excluirIds) {
        var pageable = PageRequest.of(0, quantidade);
        List<Pergunta> perguntas = testamento == null
                ? _perguntaRepository.sortearPorNivel(nivel, excluirIds, pageable)
                : _perguntaRepository.sortearPorNivelETestamento(nivel, testamento, excluirIds, pageable);

        if (perguntas.size() < quantidade) {
            String complemento = testamento == null ? "" : " no testamento " + testamento;
            throw new IllegalArgumentException(
                    "Não há perguntas suficientes de nível " + nivel + complemento + " que você ainda não respondeu."
            );
        }
        return perguntas;
    }

    private Collection<Long> perguntasJaVistas(long usuarioId) {
        List<Long> ids = _conjuntoRepository.findPerguntaIdsByUsuarioId(usuarioId);
        return ids.isEmpty() ? List.of(-1L) : ids;
    }

    private void validarPergunta(String enunciado, Testamento testamento, Nivel nivel) {
        if (enunciado == null || enunciado.isBlank()) {
            throw new IllegalArgumentException("O enunciado é obrigatório.");
        }
        if (testamento == null) {
            throw new IllegalArgumentException("O testamento é obrigatório.");
        }
        if (nivel == null) {
            throw new IllegalArgumentException("O nível é obrigatório.");
        }
    }
}
