ALTER TABLE estoque DROP CONSTRAINT ukg72w2sa50w9a647f0eyhogus5;

ALTER TABLE estoque ADD COLUMN evento_id BIGINT;

ALTER TABLE estoque
    ADD CONSTRAINT fk_estoque_evento
        FOREIGN KEY (evento_id) REFERENCES eventos(id);

CREATE UNIQUE INDEX idx_estoque_produto_evento
    ON estoque (produto_id, evento_id);

CREATE UNIQUE INDEX idx_estoque_geral_unico
    ON estoque (produto_id)
    WHERE evento_id IS NULL;