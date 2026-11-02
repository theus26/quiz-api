package com.mathe.quiz_biblico_api.dto.alternativa;

import com.mathe.quiz_biblico_api.model.Alternativa;

public record AlternativaDto(long id, String texto, short ordem) {
    public static AlternativaDto fromEntity(Alternativa alternativa) {
        return new AlternativaDto(alternativa.getId(), alternativa.getTexto(), alternativa.getOrdem());
    }
}
