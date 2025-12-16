-- Adiciona coluna evento_id na tabela caixa para relacionar caixas com eventos específicos
ALTER TABLE caixa 
ADD COLUMN evento_id BIGINT;

-- Adiciona foreign key para eventos
ALTER TABLE caixa 
ADD CONSTRAINT fk_caixa_evento 
FOREIGN KEY (evento_id) 
REFERENCES eventos(id);

-- Cria índice para melhorar performance de consultas
CREATE INDEX idx_caixa_evento_id ON caixa(evento_id);
