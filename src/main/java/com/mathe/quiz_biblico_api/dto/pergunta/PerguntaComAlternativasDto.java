package com.mathe.quiz_biblico_api.dto.pergunta;

import com.mathe.quiz_biblico_api.Enum.Nivel;
import com.mathe.quiz_biblico_api.Enum.Testamento;
import com.mathe.quiz_biblico_api.dto.alternativa.AlternativaDto;
import com.mathe.quiz_biblico_api.model.Pergunta;

import java.util.List;

public record PerguntaComAlternativasDto(
        long id,
        String enunciado,
        Testamento testamento,
        Nivel nivel,
        String referencia,
        List<AlternativaDto> alternativas
) {
    public static PerguntaComAlternativasDto fromEntity(Pergunta pergunta, List<AlternativaDto> alternativas) {
        return new PerguntaComAlternativasDto(
                pergunta.getId(),
                pergunta.getEnunciado(),
                pergunta.getTestamento(),
                pergunta.getNivel(),
                pergunta.getReferencia(),
                alternativas
        );
    }
}
