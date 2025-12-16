package br.com.geb.api.controller;

import br.com.geb.api.dto.SolicitacaoReposicaoResponseDTO;
import br.com.geb.api.dto.SolicitacaoRequest;
import br.com.geb.api.domain.estoque.SolicitacaoReposicao;
import br.com.geb.api.enums.StatusReposicao;
import br.com.geb.api.service.SolicitacaoReposicaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoReposicaoController {

    private final SolicitacaoReposicaoService service;

    public SolicitacaoReposicaoController(SolicitacaoReposicaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SolicitacaoReposicaoResponseDTO> criar(@RequestBody @Valid SolicitacaoRequest req) {
        SolicitacaoReposicao solicitacao = service.solicitarReposicao(
                req.produtoId(),
                req.quantidade(),
                req.observacao()
        );

        return ResponseEntity
                .created(URI.create("/api/solicitacoes/" + solicitacao.getId()))
                .body(new SolicitacaoReposicaoResponseDTO(solicitacao));
    }

    @GetMapping
    public ResponseEntity<List<SolicitacaoReposicaoResponseDTO>> listar(
            @RequestParam(required = false) StatusReposicao status) {

        List<SolicitacaoReposicao> resultado;

        if (status != null) {
            resultado = service.listarPorStatus(status);
        } else {
            resultado = service.listarTodos();
        }

        List<SolicitacaoReposicaoResponseDTO> dtos = resultado.stream()
                .map(SolicitacaoReposicaoResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusReposicao novoStatus) {

        SolicitacaoReposicao atualizada;

        try {
            if (novoStatus == StatusReposicao.CONCLUIDA) {
                atualizada = service.aprovar(id);
            } else if (novoStatus == StatusReposicao.REJEITADA) {
                atualizada = service.rejeitar(id);
            } else if (novoStatus == StatusReposicao.CANCELADA) {
                atualizada = service.cancelar(id);
            } else {
                return ResponseEntity.badRequest()
                        .body("Status inválido. Use CONCLUIDA, REJEITADA ou CANCELADA.");
            }

            return ResponseEntity.ok(new SolicitacaoReposicaoResponseDTO(atualizada));

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}