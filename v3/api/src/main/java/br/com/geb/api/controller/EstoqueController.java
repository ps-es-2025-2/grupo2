package br.com.geb.api.controller;

import br.com.geb.api.domain.estoque.EstoqueProduto;
import br.com.geb.api.dto.AtualizarEstoqueRequest;
import br.com.geb.api.dto.EstoqueResponseDTO;
import br.com.geb.api.service.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @GetMapping("/{produtoId}")
    public ResponseEntity<EstoqueResponseDTO> buscarEstoque(@PathVariable Long produtoId) {
        EstoqueProduto estoque = estoqueService.buscarPorProdutoId(produtoId);
        return ResponseEntity.ok(new EstoqueResponseDTO(estoque));
    }

    @PutMapping("/{produtoId}")
    public ResponseEntity<EstoqueResponseDTO> atualizarEstoque(
            @PathVariable Long produtoId,
            @RequestBody @Valid AtualizarEstoqueRequest req) {

        EstoqueProduto atualizado = estoqueService.realizarBalanco(
                produtoId,
                req.quantidadeAtual(),
                req.quantidadeMinima()
        );

        return ResponseEntity.ok(new EstoqueResponseDTO(atualizado));
    }
}