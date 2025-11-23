package com.geb.gestoreventosbebidas.controller;

import com.geb.gestoreventosbebidas.enums.TipoMovimentacaoCaixa;
import com.geb.gestoreventosbebidas.model.Caixa;
import com.geb.gestoreventosbebidas.model.MovimentacaoCaixa;
import com.geb.gestoreventosbebidas.model.Usuario; // Usado para simulação de usuário
import com.geb.gestoreventosbebidas.service.CaixaService;
import com.geb.gestoreventosbebidas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Define a classe como um Controller REST
@RequestMapping("/api/caixa") // Define o caminho base para todos os endpoints
public class CaixaController {

    @Autowired private CaixaService caixaService;
    @Autowired private UsuarioService usuarioService; // Para buscar o usuário que está operando

    /**
     * CU: Abrir Caixa
     * Exemplo de Requisição: POST /api/caixa/abrir?saldoInicial=100.00&userId=1
     */
    @PostMapping("/abrir")
    public ResponseEntity<Caixa> abrirCaixa(
            @RequestParam double saldoInicial,
            @RequestParam Long userId) {

        Usuario usuario = usuarioService.findById(userId);
        Caixa caixaAberto = caixaService.abrirCaixa(saldoInicial, usuario);
        return ResponseEntity.ok(caixaAberto);
    }

    /**
     * CU: Fechar Caixa
     * Exemplo de Requisição: POST /api/caixa/fechar?saldoFinal=5500.00&userId=1
     */
    @PostMapping("/fechar")
    public ResponseEntity<Caixa> fecharCaixa(
            @RequestParam double saldoFinal,
            @RequestParam Long userId) {

        Usuario usuario = usuarioService.findById(userId);
        Caixa caixaFechado = caixaService.fecharCaixa(saldoFinal, usuario);
        return ResponseEntity.ok(caixaFechado);
    }

    /**
     * CU: Realizar Sangria
     * Exemplo de Requisição: POST /api/caixa/sangria?valor=500.00&userId=1
     */
    @PostMapping("/sangria")
    public ResponseEntity<MovimentacaoCaixa> realizarSangria(
            @RequestParam double valor,
            @RequestParam Long userId) {

        Usuario usuario = usuarioService.findById(userId);
        MovimentacaoCaixa movimentacao = caixaService.realizarOperacao(
                TipoMovimentacaoCaixa.SANGRIA, valor, usuario);
        return ResponseEntity.ok(movimentacao);
    }

    /**
     * CU: Realizar Reforço
     * Exemplo de Requisição: POST /api/caixa/reforco?valor=200.00&userId=1
     */
    @PostMapping("/reforco")
    public ResponseEntity<MovimentacaoCaixa> realizarReforco(
            @RequestParam double valor,
            @RequestParam Long userId) {

        Usuario usuario = usuarioService.findById(userId);
        MovimentacaoCaixa movimentacao = caixaService.realizarOperacao(
                TipoMovimentacaoCaixa.REFORCO, valor, usuario);
        return ResponseEntity.ok(movimentacao);
    }
}