package br.com.geb.api.api.service;

import br.com.geb.api.domain.produto.Produto;
import br.com.geb.api.dto.ProdutoRequest;
import br.com.geb.api.enums.Categoria;
import br.com.geb.api.exception.ResourceNotFoundException;
import br.com.geb.api.repository.ProdutoRepository;
import br.com.geb.api.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    private ProdutoRepository produtoRepository;
    private ProdutoService produtoService;

    @BeforeEach
    void setUp() {
        produtoRepository = mock(ProdutoRepository.class);
        produtoService = new ProdutoService(produtoRepository);
    }

    @Test
    void deveCriarProdutoComSucesso() {
        ProdutoRequest request = new ProdutoRequest();
        request.setNome("Produto 1");
        request.setPreco(BigDecimal.valueOf(10));
        request.setCategoria(criarCategoria());

        Produto produtoSalvo = Produto.builder()
            .id(1L)
            .nome("Produto 1")
            .preco(BigDecimal.valueOf(10))
            .categoria(criarCategoria())
            .ativo(true)
            .build();

        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoSalvo);

        Produto resultado = produtoService.criar(request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Produto 1", resultado.getNome());
    }

    @Test
    void deveListarTodosProdutos() {
        Produto p1 = new Produto();
        Produto p2 = new Produto();
        when(produtoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Produto> resultado = produtoService.listar();

        assertEquals(2, resultado.size());
    }

    @Test
    void deveBuscarProdutoPorIdComSucesso() {
        Produto p = new Produto();
        p.setId(1L);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(p));

        Produto resultado = produtoService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void deveLancarExcecaoAoBuscarProdutoInexistente() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> produtoService.buscarPorId(1L));
        assertEquals("Produto não encontrado: 1", ex.getMessage());
    }

    @Test
    void deveAtualizarProdutoComSucesso() {
        ProdutoRequest request = new ProdutoRequest();
        request.setNome("Produto Atualizado");
        request.setPreco(BigDecimal.valueOf(20));

        Produto existente = Produto.builder()
            .id(1L)
            .nome("Produto Antigo")
            .preco(BigDecimal.valueOf(10))
            .build();

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(produtoRepository.save(existente)).thenReturn(existente);

        Produto resultado = produtoService.atualizar(1L, request);

        assertEquals("Produto Atualizado", resultado.getNome());
        assertEquals(BigDecimal.valueOf(20), resultado.getPreco());
    }

    @Test
    void deveLancarExcecaoAoAtualizarProdutoInexistente() {
        ProdutoRequest request = new ProdutoRequest();
        request.setNome("Produto");
        request.setPreco(BigDecimal.valueOf(10));

        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> produtoService.atualizar(1L, request));
        assertEquals("Produto não encontrado: id 1", ex.getMessage());
    }

    @Test
    void deveDeletarProdutoComSucesso() {
        Produto p = new Produto();
        p.setId(1L);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(p));
        doNothing().when(produtoRepository).delete(p);

        produtoService.deletar(1L);

        verify(produtoRepository, times(1)).delete(p);
    }

    @Test
    void deveLancarExcecaoAoDeletarProdutoInexistente() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> produtoService.deletar(1L));
        assertEquals("Produto não encontrado: id 1", ex.getMessage());
    }

    private Categoria criarCategoria() {
        return Categoria.ALCOOLICA;
    }
}
