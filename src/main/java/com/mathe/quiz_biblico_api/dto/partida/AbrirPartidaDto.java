package com.mathe.quiz_biblico_api.dto.partida;

import com.mathe.quiz_biblico_api.Enum.FiltroTestamento;
import com.mathe.quiz_biblico_api.Enum.Modalidade;
import com.mathe.quiz_biblico_api.Enum.Nivel;

public record AbrirPartidaDto(FiltroTestamento filtroTestamento, Modalidade modalidade, Nivel nivel) {
}
