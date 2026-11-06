package com.mathe.quiz_biblico_api.seed;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SemeaduraIdempotenteTest {

    @Test
    void divisorRespeitaBlocoDollar() {
        var comandos = SqlRoteiro.dividir("""
                CREATE TEMP TABLE exemplo (id int);
                DO $$
                BEGIN
                    PERFORM 1;
                END $$;
                DROP TABLE exemplo;
                """);

        assertEqualsTamanho(3, comandos.size());
        assertTrue(comandos.get(1).contains("PERFORM 1"));
    }

    @Test
    void scriptsNaoApagamPerguntasEEvitamReinsercao() throws Exception {
        for (String nome : new String[] {"perguntas.sql", "perguntas-extra.sql"}) {
            String sql = Files.readString(Path.of("src/main/resources/db").resolve(nome));
            String normalizado = sql.toLowerCase();
            assertTrue(sql.contains("NOT EXISTS"), nome);
            assertTrue(sql.contains("chave_seed"), nome);
            assertFalse(normalizado.contains("delete from pergunta"), nome);
            assertFalse(normalizado.contains("truncate"), nome);
        }
    }

    private static void assertEqualsTamanho(int esperado, int atual) {
        org.junit.jupiter.api.Assertions.assertEquals(esperado, atual);
    }
}
