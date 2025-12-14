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
    public ResponseEntity<Produto> criar(@RequestBody ProdutoRequest request) {
        Produto salvo = produtoService.criar(request);

        //Cria estoque inicial do produto (0 unidades)
        estoqueService.criarOuAtualizar(salvo.getId(), 0);

        return ResponseEntity.status(201).body(salvo);
    }

    //Busca produto pelo Id
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscar(@PathVariable Long id) {
        Produto produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produto);
    }

    //Lista todos os produtos
    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(produtoService.listar());
    }

    //Atualiza um produto
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id,
                                             @RequestBody ProdutoRequest request) {
        Produto atualizado = produtoService.atualizar(id, request);
        return ResponseEntity.ok(atualizado);
    }

    //Deletar produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
