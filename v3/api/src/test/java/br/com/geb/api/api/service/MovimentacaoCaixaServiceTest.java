package br.com.geb.api.api.service;
import br.com.geb.api.domain.caixa.Caixa;
import br.com.geb.api.domain.caixa.MovimentacaoCaixa;
import br.com.geb.api.exception.ResourceNotFoundException;
import br.com.geb.api.repository.CaixaRepository;
import br.com.geb.api.repository.MovimentacaoCaixaRepository;
import br.com.geb.api.service.MovimentacaoCaixaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovimentacaoCaixaServiceTest {

    private MovimentacaoCaixaRepository movimentacaoRepository;
    private CaixaRepository caixaRepository;
    private MovimentacaoCaixaService service;

    @BeforeEach
    void setUp() {
        movimentacaoRepository = mock(MovimentacaoCaixaRepository.class);
        caixaRepository = mock(CaixaRepository.class);
        service = new MovimentacaoCaixaService(movimentacaoRepository, caixaRepository);
    }

    @Test
    void deveCriarMovimentacaoComSucesso() {
        Caixa caixa = new Caixa();
        caixa.setId(1L);

        MovimentacaoCaixa movimentacao = new MovimentacaoCaixa();
        movimentacao.setValor(BigDecimal.TEN);

        when(caixaRepository.findById(1L)).thenReturn(Optional.of(caixa));
        when(movimentacaoRepository.save(movimentacao)).thenReturn(movimentacao);

        MovimentacaoCaixa resultado = service.criar(1L, movimentacao);

        assertNotNull(resultado);
        assertEquals(caixa, resultado.getCaixa());
        verify(movimentacaoRepository, times(1)).save(movimentacao);
    }

    @Test
    void deveLancarExceptionAoCriarMovimentacaoComCaixaInexistente() {
        MovimentacaoCaixa movimentacao = new MovimentacaoCaixa();

        when(caixaRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> service.criar(1L, movimentacao));
        assertEquals("Caixa não encontrado: 1", ex.getMessage());
    }

    @Test
    void deveBuscarMovimentacaoPorIdComSucesso() {
        MovimentacaoCaixa movimentacao = new MovimentacaoCaixa();
        movimentacao.setId(1L);

        when(movimentacaoRepository.findById(1L)).thenReturn(Optional.of(movimentacao));

        MovimentacaoCaixa resultado = service.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void deveLancarExceptionAoBuscarMovimentacaoInexistente() {
        when(movimentacaoRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(1L));
        assertEquals("Movimentação não encontrada: 1", ex.getMessage());
    }

    @Test
    void deveListarTodasMovimentacoes() {
        MovimentacaoCaixa m1 = new MovimentacaoCaixa();
        MovimentacaoCaixa m2 = new MovimentacaoCaixa();

        when(movimentacaoRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<MovimentacaoCaixa> resultado = service.listarTodos();

        assertEquals(2, resultado.size());
    }

    @Test
    void deveListarMovimentacoesPorCaixa() {
        MovimentacaoCaixa m1 = new MovimentacaoCaixa();
        MovimentacaoCaixa m2 = new MovimentacaoCaixa();

        when(movimentacaoRepository.findByCaixaId(1L)).thenReturn(Arrays.asList(m1, m2));

        List<MovimentacaoCaixa> resultado = service.listarPorCaixa(1L);

        assertEquals(2, resultado.size());
    }

    @Test
    void deveDeletarMovimentacaoComSucesso() {
        when(movimentacaoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(movimentacaoRepository).deleteById(1L);

        service.deletar(1L);

        verify(movimentacaoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExceptionAoDeletarMovimentacaoInexistente() {
        when(movimentacaoRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> service.deletar(1L));
        assertEquals("Movimentação não encontrada: 1", ex.getMessage());
    }
}
