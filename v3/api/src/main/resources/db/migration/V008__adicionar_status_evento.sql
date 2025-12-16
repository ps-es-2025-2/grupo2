-- Adiciona coluna status na tabela eventos
ALTER TABLE eventos ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'PLANEJADO';

-- Atualiza eventos existentes para ter status PLANEJADO
UPDATE eventos SET status = 'PLANEJADO' WHERE status IS NULL;
