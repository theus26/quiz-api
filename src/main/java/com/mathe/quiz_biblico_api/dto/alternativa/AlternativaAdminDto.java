package com.mathe.quiz_biblico_api.dto.alternativa;

import com.mathe.quiz_biblico_api.model.Alternativa;

public record AlternativaAdminDto(long id, String texto, boolean correta, short ordem) {
    public static AlternativaAdminDto fromEntity(Alternativa alternativa) {
        return new AlternativaAdminDto(
                alternativa.getId(),
                alternativa.getTexto(),
                alternativa.getCorreta(),
                alternativa.getOrdem()
        );
    }
}
