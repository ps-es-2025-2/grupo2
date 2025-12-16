package br.com.geb.api.service;

import br.com.geb.api.domain.evento.Evento;
import br.com.geb.api.exception.ResourceNotFoundException;
import br.com.geb.api.repository.EventoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventoService {

    private final EventoRepository repo;

    public EventoService(EventoRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Evento criar(Evento evento) {
        validarEvento(evento);
        return repo.save(evento);
    }
    /*
        A anotacao @Transactional garante que um conjunto de operações no banco de dados seja tratado como uma transação única,
        evitando dados inconsistentes e tornando a aplicação mais segura.
    */

    public Evento buscarPorId(Long id) {
        return repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com ID: " + id));
    }

    public List<Evento> listarTodos() {
        return repo.findAll();
    }

    @Transactional
    public Evento atualizar(Long id, Evento eventoAtualizado) {
        Evento existente = buscarPorId(id);

        existente.setNome(eventoAtualizado.getNome());
        existente.setDescricao(eventoAtualizado.getDescricao());
        existente.setLocal(eventoAtualizado.getLocal());
        existente.setDataInicio(eventoAtualizado.getDataInicio());
        existente.setDataFim(eventoAtualizado.getDataFim());
        existente.setStatus(eventoAtualizado.getStatus());
        validarEvento(existente);

        return repo.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        Evento existente = buscarPorId(id);
        repo.delete(existente);
    }

    private void validarEvento(Evento evento) {
        if (evento.getDataFim().isBefore(evento.getDataInicio())) {
            throw new IllegalArgumentException("A data de fim não pode ser antes da data de início");
        }
        if (evento.getDataInicio().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data de início do evento não pode ser no passado");
        }
    }
}