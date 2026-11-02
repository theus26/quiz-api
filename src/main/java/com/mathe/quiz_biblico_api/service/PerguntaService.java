package com.mathe.quiz_biblico_api.service;

import com.mathe.quiz_biblico_api.Enum.FiltroTestamento;
import com.mathe.quiz_biblico_api.Enum.Nivel;
import com.mathe.quiz_biblico_api.Enum.Testamento;
import com.mathe.quiz_biblico_api.Exception.PerguntaNotFoundException;
import com.mathe.quiz_biblico_api.dto.alternativa.AlternativaDto;
import com.mathe.quiz_biblico_api.dto.pergunta.AtualizarPerguntaDto;
import com.mathe.quiz_biblico_api.dto.pergunta.CriarPerguntaDto;
import com.mathe.quiz_biblico_api.dto.pergunta.PerguntaComAlternativasDto;
import com.mathe.quiz_biblico_api.model.Pergunta;
import com.mathe.quiz_biblico_api.repository.PerguntaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class PerguntaService {
    private static final int QUANTIDADE_DIFICIL = 5;
    private static final int QUANTIDADE_MEDIO = 5;
    private static final int QUANTIDADE_FACIL = 7;

    private final PerguntaRepository _perguntaRepository;
    private final AlternativaService _alternativaService;

    public PerguntaService(PerguntaRepository perguntaRepository, AlternativaService alternativaService) {
        this._perguntaRepository = perguntaRepository;
        this._alternativaService = alternativaService;
    }

    @Transactional(rollbackFor = Exception.class)
    public PerguntaComAlternativasDto criar(CriarPerguntaDto dto) {
        validarPergunta(dto.enunciado(), dto.testamento(), dto.nivel());
        _alternativaService.validar(dto.alternativas());

        Pergunta pergunta = _perguntaRepository.save(dto.toEntity());
        List<AlternativaDto> alternativas = _alternativaService.criar(pergunta, dto.alternativas());
        return PerguntaComAlternativasDto.fromEntity(pergunta, alternativas);
    }

    @Transactional(rollbackFor = Exception.class)
    public PerguntaComAlternativasDto atualizar(Long id, AtualizarPerguntaDto dto) throws PerguntaNotFoundException {
        validarPergunta(dto.enunciado(), dto.testamento(), dto.nivel());
        Pergunta pergunta = _perguntaRepository.findById(id).orElseThrow(PerguntaNotFoundException::new);
        pergunta.setEnunciado(dto.enunciado());
        pergunta.setTestamento(dto.testamento());
        pergunta.setNivel(dto.nivel());
        pergunta.setReferencia(dto.referencia());

        Pergunta salva = _perguntaRepository.save(pergunta);
        return PerguntaComAlternativasDto.fromEntity(salva, _alternativaService.listarPorPergunta(id));
    }

    @Transactional(readOnly = true)
    public PerguntaComAlternativasDto obter(Long id) throws PerguntaNotFoundException {
        Pergunta pergunta = _perguntaRepository.findById(id).orElseThrow(PerguntaNotFoundException::new);
        return PerguntaComAlternativasDto.fromEntity(pergunta, _alternativaService.listarPorPergunta(id));
    }

    @Transactional(readOnly = true)
    public List<PerguntaComAlternativasDto> listar(FiltroTestamento filtro) {
        Testamento testamento = filtro == null || filtro == FiltroTestamento.AMBOS
                ? null
                : Testamento.valueOf(filtro.name());

        List<Pergunta> perguntas = new ArrayList<>();
        perguntas.addAll(sortear(Nivel.DIFICIL, QUANTIDADE_DIFICIL, testamento));
        perguntas.addAll(sortear(Nivel.MEDIO, QUANTIDADE_MEDIO, testamento));
        perguntas.addAll(sortear(Nivel.FACIL, QUANTIDADE_FACIL, testamento));
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
        _alternativaService.deletarPorPergunta(id);
        _perguntaRepository.deleteById(id);
    }

    private List<Pergunta> sortear(Nivel nivel, int quantidade, Testamento testamento) {
        var pageable = PageRequest.of(0, quantidade);
        List<Pergunta> perguntas = testamento == null
                ? _perguntaRepository.sortearPorNivel(nivel, pageable)
                : _perguntaRepository.sortearPorNivelETestamento(nivel, testamento, pageable);

        if (perguntas.size() < quantidade) {
            String complemento = testamento == null ? "" : " no testamento " + testamento;
            throw new IllegalArgumentException(
                    "Não há perguntas suficientes de nível " + nivel + complemento + "."
            );
        }
        return perguntas;
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
