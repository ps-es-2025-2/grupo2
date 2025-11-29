package br.com.geb.api.service;

import br.com.geb.api.domain.caixa.Sangria;
import br.com.geb.api.repository.SangriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SangriaService {
    private final SangriaRepository repo;

    public SangriaService(SangriaRepository repo) {
        this.repo = repo;
    }

    public Sangria criar(Sangria sangria) {
        return repo.save(sangria);
    }

    public Optional<Sangria> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public List<Sangria> listarTodos() {
        return repo.findAll();
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}

