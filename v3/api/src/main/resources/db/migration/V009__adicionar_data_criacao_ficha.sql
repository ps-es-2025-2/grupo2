-- Adiciona coluna data_criacao na tabela fichas
ALTER TABLE fichas ADD COLUMN IF NOT EXISTS data_criacao TIMESTAMP;

-- Define valor padrão para fichas existentes
UPDATE fichas SET data_criacao = CURRENT_TIMESTAMP WHERE data_criacao IS NULL;
