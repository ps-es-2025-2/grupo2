package com.geb.gestoreventosbebidas.controller;

import com.geb.gestoreventosbebidas.enums.EstadoEvento;
import com.geb.gestoreventosbebidas.model.Estoque;
import com.geb.gestoreventosbebidas.model.Evento;
import com.geb.gestoreventosbebidas.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    @Autowired private EventoService eventoService;

    // CU: Gerenciar Eventos (Criação)
    @PostMapping
    public ResponseEntity<Evento> criarEvento(@RequestBody Evento evento) {
        // @RequestBody mapeia o JSON do corpo da requisição para o objeto Evento
        Evento novoEvento = eventoService.criarEvento(evento);
        return ResponseEntity.ok(novoEvento);
    }

    // CU: Gerenciar Eventos (Mudança de Estado)
    @PutMapping("/{id}/estado")
    public ResponseEntity<Evento> mudarEstado(
            @PathVariable Long id,
            @RequestParam EstadoEvento estado) {

        Evento eventoAtualizado = eventoService.mudarEstado(id, estado);
        return ResponseEntity.ok(eventoAtualizado);
    }

    /**
     * CU: Definir Estoque Inicial
     * O corpo da requisição deve ter as quantidades e IDs necessários.
     */
    @PostMapping("/{eventoId}/estoque-inicial")
    public ResponseEntity<Estoque> definirEstoqueInicial(
            @PathVariable Long eventoId,
            @RequestParam Long bebidaId,
            @RequestParam int quantidade,
            @RequestParam int quantidadeMinima) {

        Estoque estoque = eventoService.definirEstoqueInicial(
                eventoId, bebidaId, quantidade, quantidadeMinima);

        return ResponseEntity.ok(estoque);
    }
}