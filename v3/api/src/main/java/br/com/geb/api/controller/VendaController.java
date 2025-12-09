package br.com.geb.api.controller;

import br.com.geb.api.dto.VendaRequest;
import br.com.geb.api.domain.venda.Venda;
import br.com.geb.api.domain.venda.ItemVenda;
import br.com.geb.api.domain.produto.Produto;
import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.domain.cliente.Cliente;
import br.com.geb.api.service.ClienteService;
import br.com.geb.api.service.ProdutoService;
import br.com.geb.api.service.UsuarioService;
import br.com.geb.api.service.VendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    private final VendaService vendaService;
    private final ProdutoService produtoService;
    private final UsuarioService usuarioService;
    private final ClienteService clienteService;

    public VendaController(VendaService vendaService, ProdutoService produtoService, UsuarioService usuarioService, ClienteService clienteService) {
        this.vendaService = vendaService;
        this.produtoService = produtoService;
        this.usuarioService = usuarioService;
        this.clienteService = clienteService;
    }


    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody VendaRequest req) {
        Venda venda = new Venda();
        venda.setDataHora(LocalDateTime.now());

        //Sempre usa o usuário autenticado pelo token JWT
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("Usuário não autenticado");
        }

        String emailOperador = auth.getName();
        Usuario operador = usuarioService.buscarPorEmail(emailOperador);
        venda.setOperador(operador);

        //Cliente opcional
        if (req.getClienteId() != null) {
            Cliente cliente = clienteService.buscarPorId(req.getClienteId());
            venda.setCliente(cliente);
        }

        var itens = req.getItens().stream().map(i -> {
            Produto p = produtoService.buscarPorId(i.getProdutoId());
            return ItemVenda.builder()
                    .venda(venda)
                    .produto(p)
                    .quantidade(i.getQuantidade())
                    .subtotal(p.getPreco() * i.getQuantidade())
                    .build();
        }).collect(Collectors.toList());

        venda.setItens(itens);

        double total = itens.stream().mapToDouble(ItemVenda::getSubtotal).sum();
        venda.setValorTotal(total);

        Venda salvo = vendaService.registrarVenda(venda);
        return ResponseEntity.status(201).body(salvo);
    }
}
