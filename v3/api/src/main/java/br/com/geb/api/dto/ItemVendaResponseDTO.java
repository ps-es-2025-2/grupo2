package br.com.geb.api.dto;

import br.com.geb.api.domain.venda.ItemVenda;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class ItemVendaResponseDTO {
    private String nomeProduto;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;

    public ItemVendaResponseDTO(ItemVenda item) {
        if (item.getProduto() != null) {
            this.nomeProduto = item.getProduto().getNome();
            this.precoUnitario = item.getProduto().getPreco();
        }
        this.quantidade = item.getQuantidade();
        this.subtotal = item.getSubtotal();
    }
}