package br.com.geb.api.api.service;

import br.com.geb.api.domain.caixa.Sangria;
import br.com.geb.api.exception.ResourceNotFoundException;
import br.com.geb.api.repository.SangriaRepository;
import br.com.geb.api.service.SangriaService;
import br.com.geb.api.service.CaixaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SangriaServiceTest {

    private SangriaRepository sangriaRepository;
    private CaixaService caixaService;
    private SangriaService sangriaService;

    @BeforeEach
    void setUp() {
        sangriaRepository = mock(SangriaRepository.class);
        caixaService = mock(CaixaService.class);
        sangriaService = new SangriaService(sangriaRepository, caixaService);
    }

    @Test
    void deveCriarSangriaComSucesso() {
        Sangria s = new Sangria();
        s.setValor(BigDecimal.valueOf(100));
        s.setJustificativa("Teste");

        when(sangriaRepository.save(s)).thenReturn(s);

        Sangria resultado = sangriaService.criar(s);

        assertNotNull(resultado);
        assertEquals(BigDecimal.valueOf(100), resultado.getValor());
        assertEquals("Teste", resultado.getJustificativa());
    }

    @Test
    void deveListarTodasSangrias() {
        Sangria s1 = new Sangria();
        Sangria s2 = new Sangria();
        when(sangriaRepository.findAll()).thenReturn(Arrays.asList(s1, s2));

        List<Sangria> resultado = sangriaService.listarTodos();

        assertEquals(2, resultado.size());
    }

    @Test
    void deveBuscarSangriaPorIdComSucesso() {
        Sangria s = new Sangria();
        s.setId(1L);

        when(sangriaRepository.findById(1L)).thenReturn(Optional.of(s));

        Sangria resultado = sangriaService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void deveLancarExcecaoAoBuscarSangriaInexistente() {
        when(sangriaRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> sangriaService.buscarPorId(1L));
        assertEquals("Sangria não encontrada: id 1", ex.getMessage());
    }

    @Test
    void deveAtualizarSangriaComSucesso() {
        Sangria existente = new Sangria();
        existente.setId(1L);
        existente.setValor(BigDecimal.valueOf(50));
        existente.setJustificativa("Antiga");

        Sangria atualizacao = new Sangria();
        atualizacao.setValor(BigDecimal.valueOf(100));
        atualizacao.setJustificativa("Nova");

        when(sangriaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(sangriaRepository.save(existente)).thenReturn(existente);

        Sangria resultado = sangriaService.atualizar(1L, atualizacao);

        assertEquals(BigDecimal.valueOf(100), resultado.getValor());
        assertEquals("Nova", resultado.getJustificativa());
    }

    @Test
    void deveLancarExcecaoAoAtualizarSangriaInexistente() {
        Sangria atualizacao = new Sangria();
        when(sangriaRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> sangriaService.atualizar(1L, atualizacao));
        assertEquals("Sangria não encontrada: id 1", ex.getMessage());
    }

    @Test
    void deveDeletarSangriaComSucesso() {
        when(sangriaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(sangriaRepository).deleteById(1L);

        sangriaService.deletar(1L);

        verify(sangriaRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoDeletarSangriaInexistente() {
        when(sangriaRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> sangriaService.deletar(1L));
        assertEquals("Sangria não encontrada: id 1", ex.getMessage());
    }
}