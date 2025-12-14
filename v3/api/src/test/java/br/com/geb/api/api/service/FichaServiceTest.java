package br.com.geb.api.api.service;

import br.com.geb.api.domain.cliente.Cliente;
import br.com.geb.api.domain.venda.FichaDigital;
import br.com.geb.api.enums.StatusFicha;
import br.com.geb.api.repository.FichaRepository;
import br.com.geb.api.service.FichaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FichaServiceTest {

    private FichaRepository repo;
    private FichaService service;

    @BeforeEach
    void setUp() {
        repo = mock(FichaRepository.class);
        service = new FichaService(repo);
    }

    @Test
    void deveGerarFichaParaClienteComSucesso() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João");

        FichaDigital ficha = FichaDigital.builder()
            .cliente(cliente)
            .saldo(BigDecimal.ZERO)
            .limite(BigDecimal.ZERO)
            .status(StatusFicha.GERADA)
            .build();

        when(repo.save(any(FichaDigital.class))).thenReturn(ficha);

        FichaDigital resultado = service.gerarFichaParaCliente(cliente);

        assertNotNull(resultado);
        assertEquals(StatusFicha.GERADA, resultado.getStatus());
        assertEquals(BigDecimal.ZERO, resultado.getSaldo());
        verify(repo, times(1)).save(any(FichaDigital.class));
    }

    @Test
    void deveLancarExceptionSeClienteNuloAoGerarFicha() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.gerarFichaParaCliente(null));
        assertEquals("Cliente obrigatório para gerar ficha", ex.getMessage());
    }

    @Test
    void deveSalvarFichaComSucesso() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        FichaDigital ficha = FichaDigital.builder()
            .cliente(cliente)
            .saldo(BigDecimal.ZERO)
            .limite(BigDecimal.ZERO)
            .status(StatusFicha.GERADA)
            .build();

        when(repo.save(ficha)).thenReturn(ficha);

        FichaDigital resultado = service.salvar(ficha);

        assertNotNull(resultado);
        verify(repo, times(1)).save(ficha);
    }

    @Test
    void deveLancarExceptionSeFichaComClienteNulo() {
        FichaDigital ficha = FichaDigital.builder().build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.salvar(ficha));
        assertEquals("Cliente não pode ser nulo", ex.getMessage());
    }

    @Test
    void deveListarTodasFichas() {
        FichaDigital f1 = new FichaDigital();
        FichaDigital f2 = new FichaDigital();

        when(repo.findAll()).thenReturn(Arrays.asList(f1, f2));

        List<FichaDigital> resultado = service.listarTodas();

        assertEquals(2, resultado.size());
    }

    @Test
    void deveDeletarFicha() {
        FichaDigital ficha = new FichaDigital();

        doNothing().when(repo).delete(ficha);

        service.deletar(ficha);

        verify(repo, times(1)).delete(ficha);
    }

    @Test
    void deveBuscarFichaPorCodigo() {
        FichaDigital ficha = new FichaDigital();
        ficha.setCodigo("ABC123");

        when(repo.findByCodigo("ABC123")).thenReturn(Optional.of(ficha));

        Optional<FichaDigital> resultado = service.findByCodigo("ABC123");

        assertTrue(resultado.isPresent());
        assertEquals("ABC123", resultado.get().getCodigo());
    }
}
