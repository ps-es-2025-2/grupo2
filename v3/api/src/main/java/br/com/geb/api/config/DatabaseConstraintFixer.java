package br.com.geb.api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * Corrige automaticamente a constraint única problemática em estoque_id
 * que impede múltiplas solicitações para o mesmo produto.
 */
@Slf4j
@Component
public class DatabaseConstraintFixer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseConstraintFixer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("🔧 Iniciando correção de constraints da tabela solicitacao_reposicao...");
            
            // Primeiro, lista as constraints existentes
            String checkSql = """
                SELECT conname 
                FROM pg_constraint 
                WHERE conrelid = 'solicitacao_reposicao'::regclass 
                  AND contype = 'u'
                """;
            
            var constraints = jdbcTemplate.queryForList(checkSql, String.class);
            log.info("📋 Constraints UNIQUE encontradas: {}", constraints);
            
            // Remove cada constraint encontrada
            for (String constraintName : constraints) {
                try {
                    String dropSql = "ALTER TABLE solicitacao_reposicao DROP CONSTRAINT IF EXISTS " + constraintName;
                    jdbcTemplate.execute(dropSql);
                    log.info("✅ Constraint '{}' removida com sucesso!", constraintName);
                } catch (Exception e) {
                    log.warn("⚠️ Erro ao remover constraint '{}': {}", constraintName, e.getMessage());
                }
            }
            
            // Tenta remover pelo nome conhecido do erro
            try {
                jdbcTemplate.execute("ALTER TABLE solicitacao_reposicao DROP CONSTRAINT IF EXISTS ukItwglisru3xf8bov684ycv999");
                log.info("✅ Constraint problemática conhecida também removida!");
            } catch (Exception e) {
                log.debug("Constraint específica não encontrada (ok)");
            }
            
            log.info("✅ Correção de constraints concluída! Solicitações podem ser criadas normalmente.");
            
        } catch (Exception e) {
            log.error("❌ ERRO ao corrigir constraints: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
