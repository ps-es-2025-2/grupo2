package br.com.geb.api.api.service;

import br.com.geb.api.domain.cliente.Cliente;
import br.com.geb.api.domain.caixa.Caixa;
import br.com.geb.api.domain.produto.Produto;
import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.domain.venda.FichaDigital;
import br.com.geb.api.domain.venda.Venda;
import br.com.geb.api.dto.ClienteRequest;
import br.com.geb.api.dto.ItemVendaRequest;
import br.com.geb.api.dto.VendaRequest;
import br.com.geb.api.enums.StatusCaixa;
import br.com.geb.api.repository.VendaRepository;
import br.com.geb.api.service.*;
import br.com.geb.api.service.CaixaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VendaServiceTest {

    private VendaRepository vendaRepository;
    private ProdutoService produtoService;
    private ClienteService clienteService;
    private UsuarioService usuarioService;
    private FichaService fichaService;
    private EstoqueService estoqueService;
    private CaixaService caixaService;
    private VendaService vendaService;

    @BeforeEach
    void setUp() {
        vendaRepository = mock(VendaRepository.class);
        produtoService = mock(ProdutoService.class);
        clienteService = mock(ClienteService.class);
        usuarioService = mock(UsuarioService.class);
        fichaService = mock(FichaService.class);
        estoqueService = mock(EstoqueService.class);
        caixaService = mock(CaixaService.class);

        vendaService = new VendaService(
                vendaRepository, produtoService, clienteService,
                usuarioService, fichaService, estoqueService, caixaService
        );
    }


    @Test
    void deveRegistrarVendaComClienteExistente() {
        // Dados do teste
        Usuario operador = new Usuario();
        operador.setEmail("operador@test.com");

        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Produto produto = new Produto();
        produto.setId(10L);
        produto.setPreco(BigDecimal.valueOf(50));
        produto.setNome("Produto A");

        ItemVendaRequest itemRequest = new ItemVendaRequest();
        itemRequest.setProdutoId(10L);
        itemRequest.setQuantidade(2);

        VendaRequest vendaRequest = new VendaRequest();
        vendaRequest.setOperadorEmail("operador@test.com");
        vendaRequest.setClienteId(1L);
        vendaRequest.setItens(Collections.singletonList(itemRequest));

        // Mocks
        when(usuarioService.buscarPorEmail("operador@test.com")).thenReturn(operador);
        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(produtoService.buscarPorId(10L)).thenReturn(produto);
        when(fichaService.gerarFichaParaCliente(cliente)).thenReturn(new FichaDigital());
        when(vendaRepository.save(any(Venda.class))).thenAnswer(i -> i.getArgument(0));

        // Mock do caixa aberto
        Caixa caixaMock = Caixa.builder()
                .id(1L)
                .operador(operador)
                .status(StatusCaixa.ABERTO)
                .build();
        when(caixaService.buscarCaixaAbertoPorOperador(operador)).thenReturn(Optional.of(caixaMock));

        // Executa
        Venda venda = vendaService.registrarVenda(vendaRequest);

        // Verifica
        assertNotNull(venda);
        assertEquals(cliente, venda.getCliente());
        assertEquals(operador, venda.getOperador());
        assertEquals(BigDecimal.valueOf(100), venda.getValorTotal());
        assertEquals(1, venda.getItens().size());

        // Verifica se estoque foi atualizado
        verify(estoqueService, times(1)).decrementar(10L, 2);
    }

    @Test
    void deveRegistrarVendaComClienteNovo() {
        Cliente clienteNovo = new Cliente();
        clienteNovo.setId(2L);

        Usuario operador = new Usuario();
        operador.setEmail("user@test.com");

        Produto produto = new Produto();
        produto.setId(5L);
        produto.setPreco(BigDecimal.valueOf(30));
        produto.setNome("Produto B");

        ItemVendaRequest itemRequest = new ItemVendaRequest();
        itemRequest.setProdutoId(5L);
        itemRequest.setQuantidade(3);

        VendaRequest request = new VendaRequest();
        request.setOperadorEmail("user@test.com");
        request.setClienteNovo(buildRequest(clienteNovo));
        request.setItens(List.of(itemRequest));

        when(usuarioService.buscarPorEmail("user@test.com")).thenReturn(operador);
        when(clienteService.cadastrarCliente(buildRequest(clienteNovo))).thenReturn(clienteNovo);
        when(produtoService.buscarPorId(5L)).thenReturn(produto);
        when(fichaService.gerarFichaParaCliente(clienteNovo)).thenReturn(new FichaDigital());
        when(vendaRepository.save(any(Venda.class))).thenAnswer(i -> i.getArgument(0));

        // Mock do caixa aberto
        Caixa caixaMock = Caixa.builder()
                .id(2L)
                .operador(operador)
                .status(StatusCaixa.ABERTO)
                .build();
        when(caixaService.buscarCaixaAbertoPorOperador(operador)).thenReturn(Optional.of(caixaMock));

        Venda venda = vendaService.registrarVenda(request);

        assertEquals(clienteNovo, venda.getCliente());
        assertEquals(BigDecimal.valueOf(90), venda.getValorTotal());
        verify(estoqueService).decrementar(5L, 3);
    }

    @Test
    void deveLancarExcecaoSeItensEstiveremVazios() {
        VendaRequest request = new VendaRequest();
        request.setOperadorEmail("user@test.com");
        request.setClienteId(1L);
        request.setItens(Collections.emptyList());

        when(usuarioService.buscarPorEmail(anyString())).thenReturn(new Usuario());
        when(clienteService.buscarPorId(anyLong())).thenReturn(new Cliente());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> vendaService.registrarVenda(request));

        assertEquals("A venda deve conter pelo menos um item.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoSeQuantidadeDoItemForInvalida() {
        VendaRequest request = new VendaRequest();
        request.setOperadorEmail("user@test.com");
        request.setClienteId(1L);

        ItemVendaRequest item = new ItemVendaRequest();
        item.setProdutoId(1L);
        item.setQuantidade(0); // inválido
        request.setItens(List.of(item));

        when(usuarioService.buscarPorEmail(anyString())).thenReturn(new Usuario());
        when(clienteService.buscarPorId(anyLong())).thenReturn(new Cliente());
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setPreco(BigDecimal.TEN);
        when(produtoService.buscarPorId(1L)).thenReturn(produto);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> vendaService.registrarVenda(request));

        assertTrue(ex.getMessage().contains("Quantidade inválida para o produto"));
    }

    private ClienteRequest buildRequest(Cliente cliente) {
        ClienteRequest request = new ClienteRequest();
        request.setNome(cliente.getNome());
        request.setEmail(cliente.getEmail());
        request.setId(cliente.getId());
        request.setUsername(cliente.getUsername());
        request.setTelefone(cliente.getTelefone());

        return request;
    }
}