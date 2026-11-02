package com.mathe.quiz_biblico_api.dto.alternativa;

import com.mathe.quiz_biblico_api.model.Alternativa;
import com.mathe.quiz_biblico_api.model.Pergunta;

import java.util.ArrayList;
import java.util.List;

public record CriarAlternativaDto(long perguntaId, AlternativaCorretaDto[] alternativaCorretaDtos) {
    public List<Alternativa> toEntities(Pergunta pergunta, short ordemInicial) {
        List<Alternativa> alternativas = new ArrayList<>();
        for (int i = 0; i < alternativaCorretaDtos.length; i++) {
            alternativas.add(alternativaCorretaDtos[i].toEntity(pergunta, (short) (ordemInicial + i)));
        }
        return alternativas;
    }
}
