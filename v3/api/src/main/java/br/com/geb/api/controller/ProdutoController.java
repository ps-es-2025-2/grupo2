package br.com.geb.api.controller;

import br.com.geb.api.domain.produto.Produto;
import br.com.geb.api.dto.ProdutoRequest;
import br.com.geb.api.dto.ProdutoResponseDTO;
import br.com.geb.api.service.ProdutoService;
import br.com.geb.api.service.EstoqueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    /*
        O ProdutoController existe para gerenciar os produtos que a loja vende.
        Cada produto tem: nome, preco, categoria, quantidade em estoque.
        No negocio, o gerente ou administrador precisa cadastrar novos produtos na loja.
        Os funcionários precisam consultar os produtos disponíveis para vendas.
        O estoque precisa ser atualizado quando produtos entram ou saem da loja.

        O front-end (app ou site) precisa mostrar os produtos aos clientes ou operadores de caixa.
        O ProdutoController é a porta de entrada para todas essas operações no back-end.
    */

    private final ProdutoService produtoService;

    private final EstoqueService estoqueService;

    public ProdutoController(ProdutoService produtoService, EstoqueService estoqueService) {
        this.produtoService = produtoService;
        this.estoqueService = estoqueService;
    }

    //Cria produto e cria estoque
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(@RequestBody ProdutoRequest request) {
        Produto salvo = produtoService.criar(request);

        //Cria estoque inicial do produto (0 unidades)
        estoqueService.criarOuAtualizar(salvo.getId(), 0);

        ProdutoResponseDTO response = new ProdutoResponseDTO(salvo, 0);
        return ResponseEntity.status(201).body(response);
    }

    //Busca produto pelo Id
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscar(@PathVariable Long id) {
        Produto produto = produtoService.buscarPorId(id);
        Integer qtdEstoque = estoqueService.buscarQuantidadePorProdutoId(id);
        ProdutoResponseDTO response = new ProdutoResponseDTO(produto, qtdEstoque);
        return ResponseEntity.ok(response);
    }

    //Lista todos os produtos com informações de estoque
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listar() {
        List<Produto> produtos = produtoService.listar();
        List<ProdutoResponseDTO> response = produtos.stream()
                .map(p -> {
                    Integer qtd = estoqueService.buscarQuantidadePorProdutoId(p.getId());
                    return new ProdutoResponseDTO(p, qtd);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    //Atualiza um produto
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id,
                                             @RequestBody ProdutoRequest request) {
        Produto atualizado = produtoService.atualizar(id, request);
        Integer qtdEstoque = estoqueService.buscarQuantidadePorProdutoId(id);
        ProdutoResponseDTO response = new ProdutoResponseDTO(atualizado, qtdEstoque);
        return ResponseEntity.ok(response);
    }

    //Deletar produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
