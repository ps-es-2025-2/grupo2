package br.com.geb.api.dto;

import br.com.geb.api.domain.estoque.EstoqueProduto;
import lombok.Getter;

@Getter
public class EstoqueResponseDTO {
    private Long produtoId;
    private String nomeProduto;
    private Integer quantidadeAtual;
    private Integer quantidadeMinima;
    private String status;

    public EstoqueResponseDTO(EstoqueProduto estoque) {
        this.produtoId = estoque.getProduto().getId();
        this.nomeProduto = estoque.getProduto().getNome();
        this.quantidadeAtual = estoque.getQuantidadeAtual();
        this.quantidadeMinima = estoque.getQuantidadeMinima();
        this.status = calcularStatus(estoque);
    }

    private String calcularStatus(EstoqueProduto e) {
        if (e.getQuantidadeAtual() == 0) return "ESGOTADO";
        if (e.getQuantidadeAtual() <= e.getQuantidadeMinima()) return "ALERTA_BAIXO";
        return "DISPONIVEL";
    }
}