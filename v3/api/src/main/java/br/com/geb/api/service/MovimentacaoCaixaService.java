package br.com.geb.api.service;

import br.com.geb.api.domain.caixa.MovimentacaoCaixa;
import br.com.geb.api.repository.MovimentacaoCaixaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovimentacaoCaixaService {
    private final MovimentacaoCaixaRepository repo;

    public MovimentacaoCaixaService(MovimentacaoCaixaRepository repo) {
        this.repo = repo;
    }

    public MovimentacaoCaixa criar(MovimentacaoCaixa movimentacao) {
        return repo.save(movimentacao);
    }

    public Optional<MovimentacaoCaixa> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public List<MovimentacaoCaixa> listarTodos() {
        return repo.findAll();
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}

