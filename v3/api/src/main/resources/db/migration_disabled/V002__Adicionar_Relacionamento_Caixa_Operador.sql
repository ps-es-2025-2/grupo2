ALTER TABLE caixa ADD COLUMN IF NOT EXISTS operador_id BIGINT;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_caixa_operador') THEN
ALTER TABLE caixa ADD CONSTRAINT fk_caixa_operador
    FOREIGN KEY (operador_id) REFERENCES usuarios(id);
END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_caixa_operador_status
    ON caixa(operador_id, status);
