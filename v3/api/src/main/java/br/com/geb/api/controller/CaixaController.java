package br.com.geb.api.controller;

import br.com.geb.api.domain.caixa.Caixa;
import br.com.geb.api.service.CaixaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/caixa")
public class CaixaController {
    private final CaixaService service;

    public CaixaController(CaixaService service) {
        this.service = service;
    }

    @PostMapping("/abrir")
    public ResponseEntity<Caixa> abrir(@RequestParam Double saldoInicial) {
        return ResponseEntity.status(201).body(service.abrirCaixa(saldoInicial));
    }

    @PostMapping("/{id}/fechar")
    public ResponseEntity<Caixa> fechar(@PathVariable Long id, @RequestParam Double saldoFinal) {
        return ResponseEntity.ok(service.fecharCaixa(id, saldoFinal));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Caixa> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Caixa>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

