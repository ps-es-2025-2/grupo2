package br.com.geb.api.repository;

import br.com.geb.api.domain.estoque.SolicitacaoReposicao;
import br.com.geb.api.enums.StatusReposicao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SolicitacaoReposicaoRepository extends JpaRepository<SolicitacaoReposicao, Long> {
    List<SolicitacaoReposicao> findByStatus(StatusReposicao status);

    List<SolicitacaoReposicao> findByEstoqueProdutoId(Long produtoId);
}