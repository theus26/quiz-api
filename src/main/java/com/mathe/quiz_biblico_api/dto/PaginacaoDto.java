package com.mathe.quiz_biblico_api.dto;

import java.util.List;

public record PaginacaoDto<T>(List<T> dados, int totalRegistros, int totalPaginas) {

    public PaginacaoDto {
        dados = dados == null ? List.of() : List.copyOf(dados);
    }
}
