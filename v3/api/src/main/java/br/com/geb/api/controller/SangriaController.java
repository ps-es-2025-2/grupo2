package br.com.geb.api.controller;

import br.com.geb.api.domain.caixa.Sangria;
import br.com.geb.api.dto.SangriaRequest;
import br.com.geb.api.service.SangriaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sangrias")
public class SangriaController {

    /*
        Uma sangria é a retirada de dinheiro do caixa.
        Isso normalmente é feito para reduzir o dinheiro em caixa e evitar riscos, por exemplo:
        - Retirar parte do dinheiro do caixa para depósito bancário.
        - Transferir valores excedentes para outro local seguro.
        - Controlar o fluxo de caixa diário sem afetar o saldo operacional.

        Esse controller expõe endpoints que permitem ao sistema:
        - Registrar sangrias.
        - Consultar sangrias registradas.
        - Atualizar informações de uma sangria.
        - Excluir sangrias (quando necessário, por exemplo, se um lançamento for incorreto).
        - Listar todas as sangrias para conferência e auditoria.
    */

    private final SangriaService service;

    public SangriaController(SangriaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Sangria> criar(@RequestParam Long caixaId, @RequestBody @Valid SangriaRequest request) {
        Sangria sangria = Sangria.builder()
            .valor(request.getValor())
            .justificativa(request.getJustificativa())
            .operadorUsername(request.getOperadorUsername())
            .build();
        return ResponseEntity.status(201).body(service.criar(sangria, caixaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sangria> buscar(@PathVariable Long id) {
        try {
            Sangria s = service.buscarPorId(id);
            return ResponseEntity.ok(s);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Sangria>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sangria> atualizar(@PathVariable Long id,
                                             @RequestBody @Valid SangriaRequest request) {
        Sangria sangriaAtualizada = Sangria.builder()
            .valor(request.getValor())
            .justificativa(request.getJustificativa())
            .operadorUsername(request.getOperadorUsername())
            .build();
        return ResponseEntity.ok(service.atualizar(id, sangriaAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

