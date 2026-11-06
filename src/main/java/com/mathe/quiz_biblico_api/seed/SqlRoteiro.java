package com.mathe.quiz_biblico_api.seed;

import java.util.ArrayList;
import java.util.List;

public final class SqlRoteiro {
    private SqlRoteiro() {
    }

    public static List<String> dividir(String sql) {
        List<String> partes = new ArrayList<>();
        StringBuilder atual = new StringBuilder();
        boolean dollar = false;
        boolean linhaComentario = false;
        boolean texto = false;
        for (int i = 0; i < sql.length(); i++) {
            char c = sql.charAt(i);
            if (linhaComentario) {
                atual.append(c);
                if (c == '\n') {
                    linhaComentario = false;
                }
                continue;
            }
            if (!dollar && !texto && c == '-' && i + 1 < sql.length() && sql.charAt(i + 1) == '-') {
                linhaComentario = true;
                atual.append(c);
                continue;
            }
            if (!dollar && c == '\'') {
                atual.append(c);
                if (texto && i + 1 < sql.length() && sql.charAt(i + 1) == '\'') {
                    atual.append('\'');
                    i++;
                    continue;
                }
                texto = !texto;
                continue;
            }
            if (!texto && c == '$' && i + 1 < sql.length() && sql.charAt(i + 1) == '$') {
                dollar = !dollar;
                atual.append("$$");
                i++;
                continue;
            }
            if (c == ';' && !dollar && !texto) {
                adicionar(partes, atual);
                continue;
            }
            atual.append(c);
        }
        adicionar(partes, atual);
        return partes;
    }

    private static void adicionar(List<String> partes, StringBuilder atual) {
        String comando = atual.toString().trim();
        atual.setLength(0);
        if (!comando.isEmpty()) {
            partes.add(comando);
        }
    }
}
