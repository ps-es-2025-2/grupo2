package br.com.geb.api.controller;

import br.com.geb.api.domain.produto.Produto;
import br.com.geb.api.service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private final ProdutoService service;
    public ProdutoController(ProdutoService service){ this.service = service; }

    @GetMapping
    public ResponseEntity<List<Produto>> listar(){ return ResponseEntity.ok(service.listar()); }

    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody Produto p){ return ResponseEntity.status(201).body(service.criar(p)); }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscar(@PathVariable Long id){
        Produto p = service.buscar(id);
        return p==null ? ResponseEntity.notFound().build() : ResponseEntity.ok(p);
    }
}
