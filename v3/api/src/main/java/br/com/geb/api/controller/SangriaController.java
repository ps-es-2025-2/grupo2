package br.com.geb.api.controller;

import br.com.geb.api.domain.caixa.Sangria;
import br.com.geb.api.dto.SangriaRequest;
import br.com.geb.api.service.SangriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sangria")
public class SangriaController {

    private final SangriaService service;

    public SangriaController(SangriaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Sangria> criar(@RequestBody SangriaRequest request) {
        Sangria sangria = Sangria.builder()
                .valor(request.getValor())
                .justificativa(request.getJustificativa())
                .operadorUsername(request.getOperadorUsername())
                .build();
        return ResponseEntity.status(201).body(service.criar(sangria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sangria> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Sangria>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

