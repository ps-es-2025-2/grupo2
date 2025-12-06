package br.com.geb.api.controller;

import br.com.geb.api.dto.VendaRequest;
import br.com.geb.api.domain.venda.Venda;
import br.com.geb.api.domain.venda.ItemVenda;
import br.com.geb.api.domain.produto.Produto;
import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.domain.cliente.Cliente;
import br.com.geb.api.repository.ClienteRepository;
import br.com.geb.api.repository.ProdutoRepository;
import br.com.geb.api.repository.UsuarioRepository;
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
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    public VendaController(VendaService vendaService,
                           ProdutoRepository produtoRepository,
                           UsuarioRepository usuarioRepository,
                           ClienteRepository clienteRepository){
        this.vendaService = vendaService;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody VendaRequest req) {
        Venda venda = new Venda();
        venda.setDataHora(LocalDateTime.now());

        // Sempre usa o usuário autenticado pelo token JWT
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("Usuário não autenticado");
        }
        
        String emailOperador = auth.getName();
        Usuario operador = usuarioRepository.findByEmail(emailOperador)
                .orElseThrow(() -> new RuntimeException("Operador não encontrado: " + emailOperador));
        venda.setOperador(operador);

        // Cliente opcional
        if (req.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(req.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado: id " + req.getClienteId()));
            venda.setCliente(cliente);
        }

        var itens = req.getItens().stream().map(i -> {
            Produto p = produtoRepository.findById(i.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: id " + i.getProdutoId()));
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
