ALTER TABLE solicitacao_reposicao DROP CONSTRAINT IF EXISTS uk_solicitacao_estoque_id;

ALTER TABLE solicitacao_reposicao ADD COLUMN IF NOT EXISTS observacao VARCHAR(255);
ALTER TABLE solicitacao_reposicao ADD COLUMN IF NOT EXISTS data_resposta TIMESTAMP;


ALTER TABLE solicitacao_reposicao ALTER COLUMN status TYPE VARCHAR(50);