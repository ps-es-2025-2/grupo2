package br.com.geb.api.controller;

import br.com.geb.api.domain.cliente.Cliente;
import br.com.geb.api.domain.venda.FichaDigital;
import br.com.geb.api.service.ClienteService;
import br.com.geb.api.service.FichaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fichas")
public class FichaController {

    /*
        Gerencia fichas digitais de clientes.
        No negócio, uma ficha digital é um cartão virtual ou crédito associado a um cliente, que pode ter:
        - Saldo (quanto o cliente pode gastar)
        - Limite (máximo permitido)
        - Status (ex.: gerada, utilizada)
        O controller expõe operações de CRUD e validação de fichas, permitindo ao sistema de vendas ou front-end:
        - Criar fichas para clientes
        - Consultar fichas
        - Atualizar limites e saldos
        - Validar uso de fichas
        - Remover fichas
     */

    private final FichaService fichaService;

    private final ClienteService clienteService;

    public FichaController(FichaService fichaService, ClienteService clienteService) {
        this.fichaService = fichaService;
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestParam Long clienteId) {
        Cliente cliente = clienteService.buscarPorId(clienteId);

        if (cliente == null) {
            return ResponseEntity.status(404).body("Cliente não encontrado");
        }

        FichaDigital ficha = fichaService.gerarFichaParaCliente(cliente);

        return ResponseEntity.status(201).body(ficha);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<FichaDigital> buscar(@PathVariable String codigo) {
        return fichaService.findByCodigo(codigo)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{codigo}/validar")
    public ResponseEntity<?> validar(@PathVariable String codigo) {
        return fichaService.findByCodigo(codigo)
            .map(ficha -> {
                if (ficha.getStatus() == br.com.geb.api.enums.StatusFicha.UTILIZADA) {
                    return ResponseEntity.status(409).body("Ficha já usada");
                }
                ficha.setStatus(br.com.geb.api.enums.StatusFicha.UTILIZADA);
                fichaService.salvar(ficha);
                return ResponseEntity.ok(ficha);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<FichaDigital>> listar() {
        return ResponseEntity.ok(fichaService.listarTodas());
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<FichaDigital> atualizar(@PathVariable String codigo, @RequestBody FichaDigital dados) {
        return fichaService.findByCodigo(codigo)
            .map(ficha -> {
                ficha.setLimite(dados.getLimite());
                ficha.setSaldo(dados.getSaldo());
                fichaService.salvar(ficha);
                return ResponseEntity.ok(ficha);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deletar(@PathVariable String codigo) {
        fichaService.findByCodigo(codigo).ifPresent(fichaService::deletar);
        return ResponseEntity.noContent().build();
    }
}
