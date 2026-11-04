package com.mathe.quiz_biblico_api.dto.pergunta;

import com.mathe.quiz_biblico_api.Enum.Nivel;
import com.mathe.quiz_biblico_api.Enum.Testamento;
import com.mathe.quiz_biblico_api.dto.alternativa.AlternativaAdminDto;
import com.mathe.quiz_biblico_api.model.Pergunta;

import java.util.List;

public record PerguntaAdminDto(
        long id,
        String enunciado,
        Testamento testamento,
        Nivel nivel,
        String referencia,
        boolean ativa,
        List<AlternativaAdminDto> alternativas
) {
    public static PerguntaAdminDto fromEntity(Pergunta pergunta, List<AlternativaAdminDto> alternativas) {
        return new PerguntaAdminDto(
                pergunta.getId(),
                pergunta.getEnunciado(),
                pergunta.getTestamento(),
                pergunta.getNivel(),
                pergunta.getReferencia(),
                pergunta.isAtiva(),
                alternativas
        );
    }
}
