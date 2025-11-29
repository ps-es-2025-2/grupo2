package br.com.geb.api.repository;

import br.com.geb.api.domain.estoque.SolicitacaoReposicao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitacaoReposicaoRepository extends JpaRepository<SolicitacaoReposicao, Long> {}

