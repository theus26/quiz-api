package com.mathe.quiz_biblico_api.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
@Order(0)
@ConditionalOnProperty(name = "app.seed.habilitado", havingValue = "true", matchIfMissing = true)
public class SemeaduraBiblica implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(SemeaduraBiblica.class);
    private static final List<String> ROTEIROS = List.of(
            "db/evolucao.sql",
            "db/perguntas.sql",
            "db/perguntas-extra.sql"
    );

    private final DataSource dataSource;

    public SemeaduraBiblica(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) {
        try (Connection conexao = dataSource.getConnection()) {
            conexao.setAutoCommit(false);
            try (Statement travar = conexao.createStatement()) {
                travar.execute("SELECT pg_advisory_xact_lock(842017)");
            }
            long antes = contar(conexao);
            for (String roteiro : ROTEIROS) {
                executar(conexao, roteiro);
            }
            long depois = contar(conexao);
            conexao.commit();
            log.info(
                    "Semeadura bíblica concluída. Perguntas com chave de seed: {}. Novas nesta execução: {}.",
                    depois,
                    depois - antes
            );
        } catch (Exception falha) {
            log.error("Falha na semeadura de perguntas bíblicas.", falha);
            throw new IllegalStateException("Não foi possível semear as perguntas bíblicas.", falha);
        }
    }

    private void executar(Connection conexao, String caminho) throws IOException, SQLException {
        ClassPathResource recurso = new ClassPathResource(caminho);
        if (!recurso.exists()) {
            throw new IllegalStateException("Script de semeadura ausente: " + caminho);
        }
        String sql = new String(recurso.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        for (String comando : SqlRoteiro.dividir(sql)) {
            try (Statement comandoSql = conexao.createStatement()) {
                comandoSql.execute(comando);
            }
        }
        log.info("Script {} executado.", caminho);
    }

    private long contar(Connection conexao) throws SQLException {
        var ponto = conexao.setSavepoint("contagem_seed");
        try (Statement consulta = conexao.createStatement();
             ResultSet resultado = consulta.executeQuery("SELECT COUNT(*) FROM pergunta WHERE chave_seed IS NOT NULL")) {
            resultado.next();
            return resultado.getLong(1);
        } catch (SQLException falha) {
            conexao.rollback(ponto);
            return 0;
        }
    }
}
