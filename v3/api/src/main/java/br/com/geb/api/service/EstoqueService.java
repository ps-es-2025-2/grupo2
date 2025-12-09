package br.com.geb.api.service;

import br.com.geb.api.domain.estoque.EstoqueProduto;
import br.com.geb.api.domain.estoque.HistoricoMovimentacaoEstoque;
import br.com.geb.api.domain.produto.Produto;
import br.com.geb.api.enums.TipoMovimento;
import br.com.geb.api.repository.EstoqueRepository;
import br.com.geb.api.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class EstoqueService {

    private final EstoqueRepository repo;
    private final ProdutoRepository produtoRepo;

    public EstoqueService(EstoqueRepository repo, ProdutoRepository produtoRepo){
        this.repo = repo;
        this.produtoRepo = produtoRepo;
    }

    public EstoqueProduto criarOuAtualizar(Long produtoId, Integer qtd){
        Produto p = produtoRepo.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        EstoqueProduto estoque = repo.findByProdutoId(produtoId)
                .orElse(EstoqueProduto.builder()
                        .produto(p)
                        .quantidadeAtual(0)
                        .quantidadeMinima(0)
                        .historico(new ArrayList<>())
                        .build());

        estoque.setQuantidadeAtual(estoque.getQuantidadeAtual() + qtd);

        return repo.save(estoque);
    }

    public Integer buscarQuantidadePorProdutoId(Long produtoId) {
        return repo.findByProdutoId(produtoId)
                .map(EstoqueProduto::getQuantidadeAtual)
                .orElse(0);
    }


    @Transactional
    public void decrementar(Long produtoId, int quantidade){
        EstoqueProduto e = repo.findByProdutoId(produtoId)
                .orElseThrow(() -> new RuntimeException("Estoque não cadastrado para o produto id " + produtoId));
        int novaQuantidade = Math.max(0, e.getQuantidadeAtual() - quantidade);
        e.setQuantidadeAtual(novaQuantidade);

        //Registrar no histórico
        HistoricoMovimentacaoEstoque historico = HistoricoMovimentacaoEstoque.builder()
                .estoque(e)
                .tipo(TipoMovimento.SAIDA)
                .quantidade(quantidade)
                .dataHora(LocalDateTime.now())
                .origem("VENDA")
                .build();

        e.getHistorico().add(historico);
        repo.save(e);
    }

    @Transactional
    public void incrementar(Long produtoId, int quantidade, String origem){
        EstoqueProduto e = repo.findByProdutoId(produtoId).orElseThrow();
        e.setQuantidadeAtual(e.getQuantidadeAtual() + quantidade);

        // Registrar no histórico
        HistoricoMovimentacaoEstoque historico = HistoricoMovimentacaoEstoque.builder()
                .estoque(e)
                .tipo(TipoMovimento.ENTRADA)
                .quantidade(quantidade)
                .dataHora(LocalDateTime.now())
                .origem(origem)
                .build();

        if(e.getHistorico() == null) {
            e.setHistorico(new java.util.ArrayList<>());
        }
        e.getHistorico().add(historico);
        repo.save(e);
    }

    //Busca informacao do estoque por produtoId
    public EstoqueProduto buscarPorProdutoId(Long produtoId) {
        return repo.findByProdutoId(produtoId)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para o produto id " + produtoId));
    }
}
