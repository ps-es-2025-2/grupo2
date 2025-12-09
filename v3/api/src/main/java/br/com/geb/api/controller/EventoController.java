package br.com.geb.api.controller;

import br.com.geb.api.domain.evento.Evento;
import br.com.geb.api.dto.EventoRequest;
import br.com.geb.api.service.EventoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService service;

    public EventoController(EventoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Evento> criar(@RequestBody EventoRequest request) {
        Evento evento = Evento.builder()
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .dataInicio(request.getDataInicio())
                .dataFim(request.getDataFim())
                .local(request.getLocal())
                .build();
        return ResponseEntity.status(201).body(service.criar(evento));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evento> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Evento>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evento> atualizar(@PathVariable Long id, @RequestBody EventoRequest request) {
        Evento evento = Evento.builder()
                .id(id)
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .dataInicio(request.getDataInicio())
                .dataFim(request.getDataFim())
                .local(request.getLocal())
                .build();
        return ResponseEntity.ok(service.atualizar(id, evento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

