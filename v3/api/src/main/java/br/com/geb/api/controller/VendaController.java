package br.com.geb.api.controller;

import br.com.geb.api.dto.VendaRequest;
import br.com.geb.api.domain.venda.Venda;
import br.com.geb.api.domain.venda.ItemVenda;
import br.com.geb.api.domain.produto.Produto;
import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.repository.ProdutoRepository;
import br.com.geb.api.repository.UsuarioRepository;
import br.com.geb.api.service.VendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {
    private final VendaService vendaService;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    public VendaController(VendaService vendaService,
                           ProdutoRepository produtoRepository,
                           UsuarioRepository usuarioRepository){
        this.vendaService = vendaService;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody VendaRequest req) {
        Venda venda = new Venda();
        venda.setDataHora(LocalDateTime.now());

        Usuario operador = usuarioRepository.findByEmail(req.getOperadorEmail())
                .orElseThrow(() -> new RuntimeException("Operador não encontrado"));

        venda.setOperador(operador);

        var itens = req.getItens().stream().map(i -> {
            Produto p = produtoRepository.findById(i.getProdutoId()).orElseThrow();
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
