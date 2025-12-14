package br.com.geb.api.controller;

import br.com.geb.api.domain.caixa.MovimentacaoCaixa;
import br.com.geb.api.dto.MovimentacaoCaixaRequest;
import br.com.geb.api.service.MovimentacaoCaixaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimentacao-caixa")
public class MovimentacaoCaixaController {

    /*
        Gerencia movimentações de caixa, ou seja, entradas e saídas de dinheiro em um caixa de loja ou sistema financeiro interno.
        No negócio, uma movimentação de caixa representa:
        - Um valor financeiro (positivo ou negativo)
        - Um tipo de operação (ex.: entrada, saída)
        - Uma justificativa (por que a movimentação foi feita)
        Está associada a um caixa específico (Caixa aberto).

        O controller expõe operações que permitem ao sistema de gestão:
        - Registrar novas movimentações
        - Consultar movimentações
        - Listar movimentações por caixa ou globalmente
        - Excluir movimentações incorretas ou canceladas
     */

    private final MovimentacaoCaixaService service;

    public MovimentacaoCaixaController(MovimentacaoCaixaService service) {
        this.service = service;
    }

    @PostMapping("/{caixaId}")
    public ResponseEntity<MovimentacaoCaixa> criar(@PathVariable Long caixaId,
                                                   @Valid @RequestBody MovimentacaoCaixaRequest request) {
        MovimentacaoCaixa movimentacao = service.criar(caixaId, toEntity(request));
        return ResponseEntity.status(201).body(movimentacao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoCaixa> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<MovimentacaoCaixa>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/caixa/{caixaId}")
    public ResponseEntity<List<MovimentacaoCaixa>> listarPorCaixa(@PathVariable Long caixaId) {
        return ResponseEntity.ok(service.listarPorCaixa(caixaId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private MovimentacaoCaixa toEntity(MovimentacaoCaixaRequest request) {
        return MovimentacaoCaixa.builder()
            .tipo(request.getTipo())
            .valor(request.getValor())
            .justificativa(request.getJustificativa())
            .build();
    }
}

