package br.com.geb.api.service;

import br.com.geb.api.domain.produto.Produto;
import br.com.geb.api.dto.ProdutoRequest;
import br.com.geb.api.enums.Categoria;
import br.com.geb.api.exception.ResourceNotFoundException;
import br.com.geb.api.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repo;

    public ProdutoService(ProdutoRepository repo){
        this.repo = repo;
    }

    public Produto criar(ProdutoRequest request) {
        Categoria categoria;
        try {
            if (request.getCategoria() == null || request.getCategoria().isBlank()) {
                throw new IllegalArgumentException("Categoria não pode estar vazia.");
            }
            categoria = Categoria.valueOf(request.getCategoria().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Categoria inválida. Deve ser ALCOOLICA, NAO_ALCOOLICA ou COMESTIVEL."
            );
        }

        Produto p = new Produto();
        p.setNome(request.getNome());
        p.setPreco(request.getPreco());
        p.setCategoria(categoria);

        Produto salvo = repo.save(p);

        return salvo;
    }

    public List<Produto> listar(){
        return repo.findAll();
    }

    public Produto buscarPorId(Long id){
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: id " + id));
    }

}
