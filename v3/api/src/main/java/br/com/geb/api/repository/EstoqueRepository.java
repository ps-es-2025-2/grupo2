package br.com.geb.api.repository;

import br.com.geb.api.domain.estoque.EstoqueProduto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<EstoqueProduto, Long> {
    Optional<EstoqueProduto> findByProdutoId(Long id);

    List<EstoqueProduto> findByEventoId(Long eventoId);

    Optional<EstoqueProduto> findByProdutoIdAndEventoId(Long produtoId, Long eventoId);
}
