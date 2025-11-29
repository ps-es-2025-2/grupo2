package br.com.geb.api.dto;

import lombok.Data;

@Data
public class ItemVendaRequest {
    private Long produtoId;
    private Integer quantidade;
}
