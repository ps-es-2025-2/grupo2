package com.geb.gestoreventosbebidas.controller;

import com.geb.gestoreventosbebidas.model.Venda;
import com.geb.gestoreventosbebidas.model.Usuario; // Simulação de usuário
import com.geb.gestoreventosbebidas.service.VendaService;
import com.geb.gestoreventosbebidas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    @Autowired private VendaService vendaService;
    @Autowired private UsuarioService usuarioService;

    /**
     * CU: Registrar Venda & CU: Emitir Ficha/QR Code
     * Recebe um objeto Venda completo com a lista de ItemVenda no corpo da requisição.
     */
    @PostMapping
    public ResponseEntity<Venda> registrarVenda(
            @RequestBody Venda venda,
            @RequestParam Long userId) {

        // Em uma implementação real, o usuário viria do token de segurança
        Usuario usuario = usuarioService.findById(userId);

        Venda vendaRegistrada = vendaService.registrarVenda(venda, usuario);
        return ResponseEntity.ok(vendaRegistrada);
    }

    // Um endpoint para o CU: Validar Ficha (que seria uma busca simples)
    @GetMapping("/ficha/{codigoUnico}")
    public ResponseEntity<String> validarFicha(@PathVariable String codigoUnico) {
        // Lógica de validação: buscar no repository e verificar se já foi usada
        return ResponseEntity.ok("Ficha " + codigoUnico + " válida para uso.");
    }
}