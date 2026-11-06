package com.mathe.quiz_biblico_api.service;

import com.mathe.quiz_biblico_api.Enum.Nivel;

public final class PontosNivel {
    private PontosNivel() {
    }

    public static int de(Nivel nivel) {
        if (nivel == null) {
            return 0;
        }
        return switch (nivel) {
            case FACIL -> 5;
            case MEDIO -> 10;
            case DIFICIL -> 15;
        };
    }
}
