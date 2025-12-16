package br.com.geb.api.service;

import br.com.geb.api.domain.produto.Produto;
import br.com.geb.api.dto.ProdutoRequest;
import br.com.geb.api.exception.ResourceNotFoundException;
import br.com.geb.api.repository.ProdutoRepository;
import br.com.geb.api.repository.EstoqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository;

    public ProdutoService(ProdutoRepository produtoRepository, EstoqueRepository estoqueRepository) {
        this.produtoRepository = produtoRepository;
        this.estoqueRepository = estoqueRepository;
    }

    public Produto criar(ProdutoRequest request) {
        Produto produto = Produto.builder()
            .nome(request.getNome())
            .preco(request.getPreco())
            .categoria(request.getCategoria())
            .ativo(true)
            .build();

        return produtoRepository.save(produto);
    }

    public List<Produto> listar() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + id));
    }

    public Produto atualizar(Long id, ProdutoRequest request) {
        Produto produtoExistente = produtoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: id " + id));

        produtoExistente.setNome(request.getNome());
        produtoExistente.setPreco(request.getPreco());

        return produtoRepository.save(produtoExistente);
    }

    @Transactional
    public void deletar(Long id) {
        // Verifica se o produto existe
        Produto produtoExistente = produtoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: id " + id));
        
        // Primeiro deleta o estoque (se existir)
        estoqueRepository.findByProdutoId(id).ifPresent(estoque -> {
            estoqueRepository.delete(estoque);
        });
        
        // Depois deleta o produto
        produtoRepository.delete(produtoExistente);
    }

}
