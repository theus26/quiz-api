package com.mathe.quiz_biblico_api.service;

import com.mathe.quiz_biblico_api.dto.alternativa.AlternativaCorretaDto;
import com.mathe.quiz_biblico_api.dto.alternativa.AlternativaDto;
import com.mathe.quiz_biblico_api.dto.alternativa.CriarAlternativaDto;
import com.mathe.quiz_biblico_api.model.Alternativa;
import com.mathe.quiz_biblico_api.model.Pergunta;
import com.mathe.quiz_biblico_api.repository.AlternativaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AlternativaService {
    private final AlternativaRepository _alternativaRepository;

    public AlternativaService(AlternativaRepository alternativaRepository) {
        this._alternativaRepository = alternativaRepository;
    }

    public void validar(AlternativaCorretaDto[] alternativas) {
        if (alternativas == null || alternativas.length < 2) {
            throw new IllegalArgumentException("A pergunta precisa de pelo menos duas alternativas.");
        }

        int corretas = 0;
        for (AlternativaCorretaDto alternativa : alternativas) {
            if (alternativa == null || alternativa.texto() == null || alternativa.texto().isBlank()) {
                throw new IllegalArgumentException("O texto da alternativa é obrigatório.");
            }
            if (alternativa.correta()) {
                corretas++;
            }
        }
        if (corretas != 1) {
            throw new IllegalArgumentException("A pergunta deve ter uma única alternativa correta.");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public List<AlternativaDto> criar(Pergunta pergunta, AlternativaCorretaDto[] alternativas) {
        validar(alternativas);
        List<Alternativa> entidades = new CriarAlternativaDto(pergunta.getId(), alternativas)
                .toEntities(pergunta, (short) 1);
        return _alternativaRepository.saveAll(entidades).stream()
                .map(AlternativaDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AlternativaDto> listarPorPergunta(Long perguntaId) {
        return _alternativaRepository.findByPerguntaIdOrderByOrdemAsc(perguntaId).stream()
                .map(AlternativaDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public Map<Long, List<AlternativaDto>> listarPorPerguntas(Collection<Long> perguntaIds) {
        if (perguntaIds == null || perguntaIds.isEmpty()) {
            return Map.of();
        }
        return _alternativaRepository.findByPerguntaIdIn(perguntaIds).stream()
                .collect(Collectors.groupingBy(
                        alternativa -> alternativa.getPergunta().getId(),
                        Collectors.mapping(AlternativaDto::fromEntity, Collectors.toList())
                ));
    }

    @Transactional(readOnly = true)
    public boolean conferir(Long perguntaId, Long alternativaId) {
        return _alternativaRepository.buscarCorreta(perguntaId, alternativaId).orElse(false);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletarPorPergunta(Long perguntaId) {
        _alternativaRepository.deleteByPerguntaId(perguntaId);
    }
}
