package br.com.geb.api.controller;

import br.com.geb.api.domain.estoque.EstoqueProduto;
import br.com.geb.api.dto.AtualizarEstoqueRequest;
import br.com.geb.api.dto.EstoqueResponseDTO;
import br.com.geb.api.service.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    // Lista todos os estoques
    @GetMapping
    public ResponseEntity<List<EstoqueResponseDTO>> listarEstoque() {
        List<EstoqueProduto> estoques = estoqueService.listarTodos();
        List<EstoqueResponseDTO> response = estoques.stream()
                .map(EstoqueResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{produtoId}")
    public ResponseEntity<EstoqueResponseDTO> buscarEstoque(@PathVariable Long produtoId) {
        EstoqueProduto estoque = estoqueService.buscarPorProdutoId(produtoId);
        return ResponseEntity.ok(new EstoqueResponseDTO(estoque));
    }

    // Incrementar estoque (entrada de produtos)
    @PostMapping("/produto/{produtoId}/incrementar")
    public ResponseEntity<EstoqueResponseDTO> incrementarEstoque(
            @PathVariable Long produtoId,
            @RequestParam Integer quantidade,
            @RequestParam(defaultValue = "ENTRADA_MANUAL") String origem) {
        
        estoqueService.incrementar(produtoId, quantidade, origem);
        EstoqueProduto estoque = estoqueService.buscarPorProdutoId(produtoId);
        return ResponseEntity.ok(new EstoqueResponseDTO(estoque));
    }

    // Decrementar estoque (saída de produtos)
    @PostMapping("/produto/{produtoId}/decrementar")
    public ResponseEntity<EstoqueResponseDTO> decrementarEstoque(
            @PathVariable Long produtoId,
            @RequestParam Integer quantidade) {
        
        estoqueService.decrementar(produtoId, quantidade);
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