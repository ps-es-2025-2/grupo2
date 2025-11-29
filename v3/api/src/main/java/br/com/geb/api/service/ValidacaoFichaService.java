package br.com.geb.api.service;

import br.com.geb.api.domain.venda.ValidacaoFicha;
import br.com.geb.api.repository.ValidacaoFichaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ValidacaoFichaService {
    private final ValidacaoFichaRepository repo;

    public ValidacaoFichaService(ValidacaoFichaRepository repo) {
        this.repo = repo;
    }

    public ValidacaoFicha criar(ValidacaoFicha validacao) {
        return repo.save(validacao);
    }

    public Optional<ValidacaoFicha> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public List<ValidacaoFicha> listarTodos() {
        return repo.findAll();
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}

