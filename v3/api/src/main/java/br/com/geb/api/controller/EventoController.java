package br.com.geb.api.controller;

import br.com.geb.api.domain.evento.Evento;
import br.com.geb.api.dto.EventoRequest;
import br.com.geb.api.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    /*
        Responsável por gerenciar eventos de uma empresa, organização ou sistema.
    */

    private final EventoService service;

    public EventoController(EventoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Evento> criar(@RequestBody @Valid EventoRequest request) {
        Evento evento = mapToEntity(request);
        Evento criado = service.criar(evento);
        return ResponseEntity.status(201).body(criado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evento> buscar(@PathVariable Long id) {
        Evento evento = service.buscarPorId(id);
        return ResponseEntity.ok(evento);
    }

    @GetMapping
    public ResponseEntity<List<Evento>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evento> atualizar(@PathVariable Long id, @RequestBody @Valid EventoRequest request) {
        Evento evento = mapToEntity(request);
        Evento atualizado = service.atualizar(id, evento);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private Evento mapToEntity(EventoRequest request) {
        return Evento.builder()
            .nome(request.getNome())
            .descricao(request.getDescricao())
            .dataInicio(request.getDataInicio())
            .dataFim(request.getDataFim())
            .local(request.getLocal())
            .build();
    }

    /*
        Esse metodo foi criado pois esse controller lida com entrada de dados, ou seja,
        recebe um DTO (EventoRequest) e precisa transformar em uma entidade (Evento) para passar ao service.
        Deve-se mantê-lo como private dentro do controller para evitar poluição da classe e deixar o código organizado.
     */
}

