package br.com.geb.api.service;

import br.com.geb.api.domain.caixa.Caixa;
import br.com.geb.api.domain.cliente.Cliente;
import br.com.geb.api.domain.produto.Produto;
import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.domain.venda.FichaDigital;
import br.com.geb.api.domain.venda.Venda;
import br.com.geb.api.dto.VendaRequest;
import br.com.geb.api.dto.ItemVendaRequest;
import br.com.geb.api.enums.StatusCaixa;
import br.com.geb.api.repository.VendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes do Relacionamento Venda ↔ Caixa")
@ExtendWith(MockitoExtension.class)
public class VendaCaixaIntegrationTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private CaixaService caixaService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private FichaService fichaService;

    @Mock
    private EstoqueService estoqueService;

    private VendaService vendaService;

    private Usuario operador;
    private Caixa caixaAberto;
    private VendaRequest vendaRequest;

    @BeforeEach
    void setUp() {

        vendaService = new VendaService(
            vendaRepository, produtoService, clienteService,
            usuarioService, fichaService, estoqueService, caixaService
        );

        // Preparar dados de teste
        operador = new Usuario();
        operador.setId(1L);
        operador.setEmail("operador@test.com");

        caixaAberto = Caixa.builder()
                .id(1L)
                .operador(operador)
                .status(StatusCaixa.ABERTO)
                .saldoInicial(new BigDecimal("500.00"))
                .build();

        vendaRequest = new VendaRequest();
        vendaRequest.setOperadorEmail("operador@test.com");
        vendaRequest.setClienteId(1L);

        ItemVendaRequest item = new ItemVendaRequest();
        item.setProdutoId(1L);
        item.setQuantidade(1);
        vendaRequest.setItens(new ArrayList<>());
        vendaRequest.getItens().add(item);
    }

    @Test
    @DisplayName("Deve vincular venda ao caixa aberto do operador")
    void testRegistrarVendaVinculaCaixa() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");

        when(usuarioService.buscarPorEmail(operador.getEmail())).thenReturn(operador);
        when(caixaService.buscarCaixaAbertoPorOperador(operador))
                .thenReturn(Optional.of(caixaAberto));
        when(clienteService.buscarPorId(any())).thenReturn(cliente);

        Produto produtoMock = new Produto();
        produtoMock.setId(1L);
        produtoMock.setPreco(new BigDecimal("100.00"));
        when(produtoService.buscarPorId(any())).thenReturn(produtoMock);

        when(fichaService.gerarFichaParaCliente(any())).thenReturn(new FichaDigital());
        doNothing().when(estoqueService).decrementar(any(), anyInt());
        when(vendaRepository.save(any(Venda.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Venda venda = vendaService.registrarVenda(vendaRequest);

        // Assert
        assertNotNull(venda);
        assertEquals(caixaAberto, venda.getCaixa());
        assertEquals(operador, venda.getOperador());
        verify(caixaService, times(1)).buscarCaixaAbertoPorOperador(operador);
    }

    @Test
    @DisplayName("Deve lançar exceção quando não há caixa aberto")
    void testRegistrarVendaSemCaixaAberto() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");

        when(usuarioService.buscarPorEmail(operador.getEmail())).thenReturn(operador);
        when(clienteService.buscarPorId(any())).thenReturn(cliente);

        Produto produtoMock = new Produto();
        produtoMock.setId(1L);
        produtoMock.setPreco(new BigDecimal("100.00"));
        when(produtoService.buscarPorId(any())).thenReturn(produtoMock);

        when(caixaService.buscarCaixaAbertoPorOperador(operador))
                .thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> vendaService.registrarVenda(vendaRequest)
        );

        assertTrue(exception.getMessage().contains("Não existe caixa aberto"));
        verify(vendaRepository, never()).save(any(Venda.class));
    }

    @Test
    @DisplayName("Deve buscar caixa aberto do operador corretamente")
    void testBuscarCaixaAbertoPorOperador() {
        // Arrange
        when(caixaService.buscarCaixaAbertoPorOperador(operador))
                .thenReturn(Optional.of(caixaAberto));

        // Act
        Optional<Caixa> resultado = caixaService.buscarCaixaAbertoPorOperador(operador);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(caixaAberto, resultado.get());
        assertEquals(operador, resultado.get().getOperador());
        assertEquals(StatusCaixa.ABERTO, resultado.get().getStatus());
    }

    @Test
    @DisplayName("Deve abrir caixa com operador vinculado")
    void testAbrirCaixaComOperador() {
        // Arrange
        BigDecimal saldoInicial = new BigDecimal("500.00");
        Caixa caixaEsperado = Caixa.builder()
                .id(1L)
                .operador(operador)
                .status(StatusCaixa.ABERTO)
                .saldoInicial(saldoInicial)
                .build();

        when(caixaService.abrirCaixa(saldoInicial, operador))
                .thenReturn(caixaEsperado);

        // Act
        Caixa caixa = caixaService.abrirCaixa(saldoInicial, operador);

        // Assert
        assertNotNull(caixa);
        assertEquals(operador, caixa.getOperador());
        assertEquals(StatusCaixa.ABERTO, caixa.getStatus());
        assertEquals(saldoInicial, caixa.getSaldoInicial());
        verify(caixaService, times(1)).abrirCaixa(saldoInicial, operador);
    }

    @Test
    @DisplayName("Deve validar que venda pertence ao caixa correto")
    void testVendaPertenceCaixaCorreto() {
        // Arrange
        Venda venda = new Venda();
        venda.setId(1L);
        venda.setOperador(operador);
        venda.setCaixa(caixaAberto);

        // Act & Assert
        assertEquals(caixaAberto.getId(), venda.getCaixa().getId());
        assertEquals(caixaAberto.getOperador().getId(), venda.getOperador().getId());
    }
}

