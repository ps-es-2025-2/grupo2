ALTER TABLE solicitacao_reposicao
DROP CONSTRAINT IF EXISTS solicitacao_reposicao_status_check;

ALTER TABLE solicitacao_reposicao
    ADD CONSTRAINT solicitacao_reposicao_status_check
        CHECK (status IN (
                          'PENDENTE',
                          'CONCLUIDA',
                          'CANCELADA',
                          'REJEITADA'
            ));
