package br.com.geb.api.service;

import br.com.geb.api.domain.produto.Produto;
import br.com.geb.api.dto.ProdutoRequest;
import br.com.geb.api.exception.ResourceNotFoundException;
import br.com.geb.api.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
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

    public void deletar(Long id) {
        Produto produtoExistente = produtoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: id " + id));
        produtoRepository.delete(produtoExistente);
    }

}
