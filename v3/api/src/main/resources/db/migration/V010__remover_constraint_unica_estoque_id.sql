-- Remove a constraint única de estoque_id da tabela solicitacao_reposicao
-- Isso permite múltiplas solicitações para o mesmo produto/estoque

-- Tenta remover várias possíveis variações do nome da constraint
ALTER TABLE solicitacao_reposicao DROP CONSTRAINT IF EXISTS uk_solicitacao_estoque_id;
ALTER TABLE solicitacao_reposicao DROP CONSTRAINT IF EXISTS ukItwglisru3xf8bov684ycv999;
ALTER TABLE solicitacao_reposicao DROP CONSTRAINT IF EXISTS uk_ltw_glisru3xf8bov684ycv999;
ALTER TABLE solicitacao_reposicao DROP CONSTRAINT IF EXISTS uk9xf8bov684ycv999;

-- Consulta para verificar constraints restantes (apenas para log)
-- SELECT conname FROM pg_constraint WHERE conrelid = 'solicitacao_reposicao'::regclass AND contype = 'u';
