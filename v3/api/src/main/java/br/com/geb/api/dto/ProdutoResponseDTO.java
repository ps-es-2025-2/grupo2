package br.com.geb.api.dto;

import br.com.geb.api.domain.produto.Produto;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private BigDecimal preco;
    private String categoria;
    private Boolean ativo;
    private Integer quantidadeEstoque;

    public ProdutoResponseDTO(Produto produto, Integer quantidadeEstoque) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.preco = produto.getPreco();
        this.categoria = produto.getCategoria() != null ? produto.getCategoria().name() : null;
        this.ativo = produto.getAtivo();
        this.quantidadeEstoque = quantidadeEstoque != null ? quantidadeEstoque : 0;
    }
}
