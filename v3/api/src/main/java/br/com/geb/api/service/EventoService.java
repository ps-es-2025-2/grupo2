package br.com.geb.api.service;

import br.com.geb.api.domain.evento.Evento;
import br.com.geb.api.repository.EventoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventoService {
    private final EventoRepository repo;

    public EventoService(EventoRepository repo) {
        this.repo = repo;
    }

    public Evento criar(Evento evento) {
        return repo.save(evento);
    }

    public Optional<Evento> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public List<Evento> listarTodos() {
        return repo.findAll();
    }

    public Evento atualizar(Long id, Evento evento) {
        evento.setId(id);
        return repo.save(evento);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}

