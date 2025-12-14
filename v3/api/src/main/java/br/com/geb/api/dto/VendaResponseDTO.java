package br.com.geb.api.dto;

import br.com.geb.api.domain.venda.Venda;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class VendaResponseDTO {
    private final Long id;
    private final LocalDateTime dataHora;
    private final BigDecimal valorTotal;

    private final String nomeCliente;
    private String nomeOperador;
    private Long idCaixa;
    private String nomeEvento;

    private List<ItemVendaResponseDTO> itens;

    public VendaResponseDTO(Venda venda) {
        this.id = venda.getId();
        this.dataHora = venda.getDataHora();
        this.valorTotal = venda.getValorTotal();

        if (venda.getCliente() != null) {
            this.nomeCliente = venda.getCliente().getNome();
        } else {
            this.nomeCliente = "Cliente Não Identificado";
        }

        if (venda.getOperador() != null) {
            this.nomeOperador = venda.getOperador().getNome();
        }

        if (venda.getCaixa() != null) {
            this.idCaixa = venda.getCaixa().getId();
        }

        if (venda.getEvento() != null) {
            this.nomeEvento = venda.getEvento().getNome();
        }

        if (venda.getItens() != null) {
            this.itens = venda.getItens().stream()
                    .map(ItemVendaResponseDTO::new)
                    .collect(Collectors.toList());
        }
    }
}