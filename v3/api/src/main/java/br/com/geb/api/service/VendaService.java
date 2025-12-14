package br.com.geb.api.service;

import br.com.geb.api.domain.caixa.Caixa;
import br.com.geb.api.domain.cliente.Cliente;
import br.com.geb.api.domain.produto.Produto;
import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.domain.venda.FichaDigital;
import br.com.geb.api.domain.venda.ItemVenda;
import br.com.geb.api.domain.venda.Venda;
import br.com.geb.api.dto.VendaRequest;
import br.com.geb.api.repository.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoService produtoService;
    private final ClienteService clienteService;
    private final UsuarioService usuarioService;
    private final FichaService fichaService;
    private final EstoqueService estoqueService;
    private final CaixaService caixaService;

    public VendaService(VendaRepository vendaRepository,
                        ProdutoService produtoService,
                        ClienteService clienteService,
                        UsuarioService usuarioService,
                        FichaService fichaService,
                        EstoqueService estoqueService,
                        CaixaService caixaService) {
        this.vendaRepository = vendaRepository;
        this.produtoService = produtoService;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
        this.fichaService = fichaService;
        this.estoqueService = estoqueService;
        this.caixaService = caixaService;
    }

    @Transactional
    public Venda registrarVenda(VendaRequest request) {
        Usuario operador = usuarioService.buscarPorEmail(request.getOperadorEmail());
        Cliente cliente = resolverCliente(request);

        validarItens(request);

        Venda venda = criarVendaBase(cliente, operador);

        var itens = criarItensVenda(request, venda);
        venda.setItens(itens);

        calcularTotal(venda);
        gerarFicha(venda);

        // Vincular o caixa aberto do operador
        Caixa caixaAberto = caixaService.buscarCaixaAbertoPorOperador(operador)
            .orElseThrow(() -> new IllegalArgumentException(
                "Não existe caixa aberto para o operador: " + operador.getEmail()
            ));
        venda.setCaixa(caixaAberto);

        Venda vendaSalva = vendaRepository.save(venda);

        atualizarEstoque(itens);

        return vendaSalva;
    }

    private Cliente resolverCliente(VendaRequest request) {
        if (request.getClienteId() != null) {
            //Se existe ID → busca no banco
            return clienteService.buscarPorId(request.getClienteId());
        }

        if (request.getClienteNovo() != null) {
            //Se não existe ID, mas existe clienteNovo → cadastra
            return clienteService.cadastrarCliente(request.getClienteNovo());
        }

        throw new IllegalArgumentException(
            "É necessário informar o ID do cliente ou os dados de um novo cliente."
        );
    }

    private void validarItens(VendaRequest request) {
        if (request.getItens() == null || request.getItens().isEmpty()) {
            throw new IllegalArgumentException("A venda deve conter pelo menos um item.");
        }
    }

    private Venda criarVendaBase(Cliente cliente, Usuario operador) {
        return Venda.builder()
            .cliente(cliente)
            .operador(operador)
            .dataHora(LocalDateTime.now())
            .build();
    }

    private java.util.List<ItemVenda> criarItensVenda(VendaRequest request, Venda venda) {
        return request.getItens().stream()
            .map(i -> {
                Produto produto = produtoService.buscarPorId(i.getProdutoId());

                if (i.getQuantidade() <= 0) {
                    throw new IllegalArgumentException("Quantidade inválida para o produto: " + produto.getNome());
                }

                return ItemVenda.builder()
                    .produto(produto)
                    .quantidade(i.getQuantidade())
                    .subtotal(produto.getPreco().multiply(BigDecimal.valueOf(i.getQuantidade())))
                    .venda(venda)
                    .build();
            })
            .collect(Collectors.toList());
    }

    private void calcularTotal(Venda venda) {
        BigDecimal total = venda.getItens().stream()
            .map(ItemVenda::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        venda.setValorTotal(total);
    }

    private void gerarFicha(Venda venda) {
        FichaDigital ficha = fichaService.gerarFichaParaCliente(venda.getCliente());
        venda.setFicha(ficha);
    }

    private void atualizarEstoque(java.util.List<ItemVenda> itens) {
        itens.forEach(i ->
            estoqueService.decrementar(i.getProduto().getId(), i.getQuantidade())
        );
    }
}