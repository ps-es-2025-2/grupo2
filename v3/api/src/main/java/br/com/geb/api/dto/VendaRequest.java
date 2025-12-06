package br.com.geb.api.dto;

import lombok.Data;
import java.util.List;

@Data
public class VendaRequest {
    private Long clienteId;
    private String operadorEmail;
    private List<ItemVendaRequest> itens;
}
