package br.com.geb.api.controller;

import br.com.geb.api.domain.produto.Produto;
import br.com.geb.api.dto.ProdutoRequest;
import br.com.geb.api.service.ProdutoService;
import br.com.geb.api.service.EstoqueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService service;
    private final EstoqueService estoqueService;

    public ProdutoController(ProdutoService service, EstoqueService estoqueService) {
        this.service = service;
        this.estoqueService = estoqueService;
    }

    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody ProdutoRequest request) {
        // O service já faz a validação da categoria e criação do produto
        Produto salvo = service.criar(request);

        return ResponseEntity.status(201).body(salvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscar(@PathVariable Long id) {
        Produto p = service.buscarPorId(id);
        return p == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(p);
    }
}
