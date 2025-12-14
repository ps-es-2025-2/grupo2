package br.com.geb.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class VendaRequest {

    // Cliente existente (preencher se não for cliente novo)
    private Long clienteId;

    // Cliente novo (preencher se for venda para cliente não cadastrado)
    @Valid
    private ClienteRequest clienteNovo;

    // Email do operador que registra a venda (geralmente obtido via JWT, mas pode ser opcional aqui)
    private String operadorEmail;

    // Itens da venda: obrigatório ter pelo menos 1 item
    @NotEmpty(message = "A venda deve ter pelo menos um item.")
    @Valid
    private List<ItemVendaRequest> itens;

}
