package br.com.geb.api.dto;

import br.com.geb.api.domain.estoque.EstoqueProduto;
import br.com.geb.api.domain.produto.Produto;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class EstoqueResponseDTO {
    private Long id;
    private ProdutoSimplificado produto;
    private Integer quantidadeAtual;
    private Integer quantidadeMinima;
    private String status;

    public EstoqueResponseDTO(EstoqueProduto estoque) {
        this.id = estoque.getId();
        this.produto = new ProdutoSimplificado(estoque.getProduto());
        this.quantidadeAtual = estoque.getQuantidadeAtual();
        this.quantidadeMinima = estoque.getQuantidadeMinima();
        this.status = calcularStatus(estoque);
    }

    private String calcularStatus(EstoqueProduto e) {
        if (e.getQuantidadeAtual() == 0) return "ESGOTADO";
        if (e.getQuantidadeAtual() <= e.getQuantidadeMinima()) return "ALERTA_BAIXO";
        return "DISPONIVEL";
    }

    @Getter
    public static class ProdutoSimplificado {
        private Long id;
        private String nome;
        private String categoria;
        private BigDecimal preco;

        public ProdutoSimplificado(Produto produto) {
            this.id = produto.getId();
            this.nome = produto.getNome();
            this.categoria = produto.getCategoria() != null ? produto.getCategoria().name() : null;
            this.preco = produto.getPreco();
        }
    }
}