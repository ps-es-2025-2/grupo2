package br.com.geb.api.repository;

import br.com.geb.api.domain.estoque.HistoricoMovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoMovimentacaoEstoqueRepository extends JpaRepository<HistoricoMovimentacaoEstoque, Long> {}

