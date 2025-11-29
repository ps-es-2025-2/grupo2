package br.com.geb.api.service;

import br.com.geb.api.domain.produto.Produto;
import br.com.geb.api.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository repo;

    public ProdutoService(ProdutoRepository repo){
        this.repo = repo;
    }

    public Produto criar(Produto p){
        return repo.save(p);
    }

    public List<Produto> listar(){
        return repo.findAll();
    }

    public Produto buscar(Long id){
        return repo.findById(id).orElse(null);
    }
}
