package br.com.geb.api.controller;

import br.com.geb.api.domain.caixa.MovimentacaoCaixa;
import br.com.geb.api.dto.MovimentacaoCaixaRequest;
import br.com.geb.api.service.CaixaService;
import br.com.geb.api.service.MovimentacaoCaixaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/movimentacao-caixa")
public class MovimentacaoCaixaController {
    private final MovimentacaoCaixaService service;
    private final CaixaService caixaService;

    public MovimentacaoCaixaController(MovimentacaoCaixaService service, CaixaService caixaService) {
        this.service = service;
        this.caixaService = caixaService;
    }

    @PostMapping
    public ResponseEntity<MovimentacaoCaixa> criar(@RequestBody MovimentacaoCaixaRequest request) {
        var caixa = caixaService.buscarPorId(request.getCaixaId())
                .orElseThrow(() -> new RuntimeException("Caixa não encontrada"));

        MovimentacaoCaixa movimentacao = MovimentacaoCaixa.builder()
                .caixa(caixa)
                .tipo(request.getTipo())
                .valor(request.getValor())
                .justificativa(request.getJustificativa())
                .dataHora(LocalDateTime.now())
                .build();
        return ResponseEntity.status(201).body(service.criar(movimentacao));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoCaixa> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<MovimentacaoCaixa>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

