package br.com.geb.api.service;

import br.com.geb.api.domain.estoque.EstoqueProduto;
import br.com.geb.api.domain.estoque.HistoricoMovimentacaoEstoque;
import br.com.geb.api.domain.produto.Produto;
import br.com.geb.api.enums.TipoMovimento;
import br.com.geb.api.exception.ResourceNotFoundException;
import br.com.geb.api.repository.EstoqueRepository;
import br.com.geb.api.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;

    public EstoqueService(EstoqueRepository estoqueRepository, ProdutoRepository produtoRepository) {
        this.estoqueRepository = estoqueRepository;
        this.produtoRepository = produtoRepository;

    }

    // Cria estoque se não existir ou atualiza se existir
    @Transactional
    public EstoqueProduto criarOuAtualizar(Long produtoId, Integer qtd) {

        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + produtoId));

        EstoqueProduto estoque = estoqueRepository.findByProdutoId(produtoId)
            .orElseGet(() -> novoEstoque(produto));

        estoque.setQuantidadeAtual(estoque.getQuantidadeAtual() + qtd);

        registrarMovimento(estoque, TipoMovimento.ENTRADA, qtd, "AJUSTE");

        return estoqueRepository.save(estoque);
    }

    // Busca quantidade disponível de um produto específico
    public int buscarQuantidadePorProdutoId(Long produtoId) {
        return estoqueRepository.findByProdutoId(produtoId)
            .map(EstoqueProduto::getQuantidadeAtual)
            .orElse(0);
    }

    // Movimenta saída de estoque (vendas)
    @Transactional
    public void decrementar(Long produtoId, int quantidade) {
        EstoqueProduto estoque = estoqueRepository.findByProdutoId(produtoId)
            .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado: " + produtoId));

        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }

        int novaQuantidade = estoque.getQuantidadeAtual() - quantidade;

        if (novaQuantidade < 0) {
            throw new IllegalArgumentException("Estoque insuficiente para o produto id " + produtoId);
        }

        estoque.setQuantidadeAtual(novaQuantidade);

        registrarMovimento(estoque, TipoMovimento.SAIDA, quantidade, "VENDA");

        estoqueRepository.save(estoque);
    }

    // Movimenta entrada no estoque (compras/reabastecimento)
    @Transactional
    public void incrementar(Long produtoId, int quantidade, String origem) {

        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }

        EstoqueProduto estoque = estoqueRepository.findByProdutoId(produtoId)
            .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado: " + produtoId));

        estoque.setQuantidadeAtual(estoque.getQuantidadeAtual() + quantidade);

        registrarMovimento(estoque, TipoMovimento.ENTRADA, quantidade, origem);

        estoqueRepository.save(estoque);
    }

    // Busca estoque completo por id
    public EstoqueProduto buscarPorProdutoId(Long produtoId) {
        return estoqueRepository.findByProdutoId(produtoId)
            .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado para o produto id " + produtoId));
    }

    // Cria um estoque novo com valores default
    private EstoqueProduto novoEstoque(Produto produto) {
        return EstoqueProduto.builder()
            .produto(produto)
            .quantidadeAtual(0)
            .quantidadeMinima(0)
            .historico(new ArrayList<>())
            .build();
    }

    // Registrar movimentação no histórico
    private void registrarMovimento(EstoqueProduto estoque,
                                    TipoMovimento tipo,
                                    int quantidade,
                                    String origem) {
        HistoricoMovimentacaoEstoque h = HistoricoMovimentacaoEstoque.builder()
            .estoque(estoque)
            .tipo(tipo)
            .quantidade(quantidade)
            .dataHora(LocalDateTime.now())
            .origem(origem)
            .build();

        if (estoque.getHistorico() == null) {
            estoque.setHistorico(new ArrayList<>());
        }

        estoque.getHistorico().add(h);
    }

    @Transactional
    public EstoqueProduto realizarBalanco(Long produtoId, Integer novaQuantidadeFisica, Integer novaQuantidadeMinima) {
        EstoqueProduto estoque = estoqueRepository.findByProdutoId(produtoId)
                .orElseGet(() -> {
                    Produto p = produtoRepository.findById(produtoId)
                            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + produtoId));
                    return novoEstoque(p);
                });

        int diferenca = novaQuantidadeFisica - estoque.getQuantidadeAtual();

        if (diferenca > 0) {
            registrarMovimento(estoque, TipoMovimento.ENTRADA, diferenca, "AJUSTE_BALANCO");
        } else if (diferenca < 0) {
            registrarMovimento(estoque, TipoMovimento.SAIDA, Math.abs(diferenca), "AJUSTE_BALANCO");
        }

        estoque.setQuantidadeAtual(novaQuantidadeFisica);
        estoque.setQuantidadeMinima(novaQuantidadeMinima);

        return estoqueRepository.save(estoque);
    }
}
