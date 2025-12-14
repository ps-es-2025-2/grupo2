package br.com.geb.api.controller;

import br.com.geb.api.dto.VendaRequest;
import br.com.geb.api.domain.venda.Venda;
import br.com.geb.api.dto.VendaResponseDTO;
import br.com.geb.api.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    /*
        Uma venda representa a operação de comercialização de produtos para um cliente.
        Cada venda envolve:
        - Um cliente que compra os produtos.
        - Um operador (usuário do sistema) que registra a venda.
        - Um conjunto de itens (produtos + quantidade + subtotal).
        - O cálculo do valor total da venda.

        Registrar a venda envolve persistir esses dados no banco e atualizar o estoque dos produtos.
        Essa classe VendaController serve como a porta de entrada do front-end para o módulo de vendas.
     */

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody VendaRequest req) {
        Venda vendaSalva = vendaService.registrarVenda(req);

        VendaResponseDTO response = new VendaResponseDTO(vendaSalva);

        return ResponseEntity
                .created(URI.create("/api/vendas/" + vendaSalva.getId()))
                .body(response);
    }
}