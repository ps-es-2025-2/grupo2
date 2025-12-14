package br.com.geb.api.api.service;

import br.com.geb.api.domain.estoque.EstoqueProduto;
import br.com.geb.api.domain.produto.Produto;
import br.com.geb.api.enums.TipoMovimento;
import br.com.geb.api.exception.ResourceNotFoundException;
import br.com.geb.api.repository.EstoqueRepository;
import br.com.geb.api.repository.ProdutoRepository;
import br.com.geb.api.service.EstoqueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstoqueServiceTest {

    private EstoqueRepository estoqueRepository;
    private ProdutoRepository produtoRepository;
    private EstoqueService estoqueService;

    @BeforeEach
    void setUp() {
        estoqueRepository = mock(EstoqueRepository.class);
        produtoRepository = mock(ProdutoRepository.class);
        estoqueService = new EstoqueService(estoqueRepository, produtoRepository);
    }

    @Test
    void deveCriarOuAtualizarEstoqueExistente() {
        Produto produto = Produto.builder().id(1L).nome("Produto A").build();
        EstoqueProduto estoque = EstoqueProduto.builder()
            .produto(produto)
            .quantidadeAtual(10)
            .historico(new ArrayList<>())
            .build();

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(estoqueRepository.findByProdutoId(1L)).thenReturn(Optional.of(estoque));
        when(estoqueRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        EstoqueProduto resultado = estoqueService.criarOuAtualizar(1L, 5);

        assertEquals(15, resultado.getQuantidadeAtual());
        assertEquals(1, resultado.getHistorico().size());
        assertEquals(TipoMovimento.ENTRADA, resultado.getHistorico().get(0).getTipo());
    }

    @Test
    void deveCriarNovoEstoqueSeNaoExistir() {
        Produto produto = Produto.builder().id(1L).nome("Produto B").build();

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(estoqueRepository.findByProdutoId(1L)).thenReturn(Optional.empty());
        when(estoqueRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        EstoqueProduto resultado = estoqueService.criarOuAtualizar(1L, 10);

        assertEquals(10, resultado.getQuantidadeAtual());
        assertEquals(produto, resultado.getProduto());
        assertEquals(1, resultado.getHistorico().size());
        assertEquals("AJUSTE", resultado.getHistorico().get(0).getOrigem());
    }

    @Test
    void deveDecrementarEstoqueComSucesso() {
        Produto produto = Produto.builder().id(1L).build();
        EstoqueProduto estoque = EstoqueProduto.builder().produto(produto).quantidadeAtual(20).historico(new ArrayList<>()).build();

        when(estoqueRepository.findByProdutoId(1L)).thenReturn(Optional.of(estoque));
        when(estoqueRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        estoqueService.decrementar(1L, 5);

        assertEquals(15, estoque.getQuantidadeAtual());
        assertEquals(1, estoque.getHistorico().size());
        assertEquals(TipoMovimento.SAIDA, estoque.getHistorico().get(0).getTipo());
    }

    @Test
    void deveLancarExceptionSeEstoqueInsuficiente() {
        Produto produto = Produto.builder().id(1L).build();
        EstoqueProduto estoque = EstoqueProduto.builder().produto(produto).quantidadeAtual(3).historico(new ArrayList<>()).build();

        when(estoqueRepository.findByProdutoId(1L)).thenReturn(Optional.of(estoque));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            estoqueService.decrementar(1L, 5)
        );

        assertEquals("Estoque insuficiente para o produto id 1", ex.getMessage());
    }

    @Test
    void deveIncrementarEstoqueComSucesso() {
        Produto produto = Produto.builder().id(1L).build();
        EstoqueProduto estoque = EstoqueProduto.builder().produto(produto).quantidadeAtual(10).historico(new ArrayList<>()).build();

        when(estoqueRepository.findByProdutoId(1L)).thenReturn(Optional.of(estoque));
        when(estoqueRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        estoqueService.incrementar(1L, 5, "COMPRA");

        assertEquals(15, estoque.getQuantidadeAtual());
        assertEquals(1, estoque.getHistorico().size());
        assertEquals("COMPRA", estoque.getHistorico().get(0).getOrigem());
    }

    @Test
    void deveBuscarQuantidadePorProdutoId() {
        Produto produto = Produto.builder().id(1L).build();
        EstoqueProduto estoque = EstoqueProduto.builder().produto(produto).quantidadeAtual(12).build();

        when(estoqueRepository.findByProdutoId(1L)).thenReturn(Optional.of(estoque));

        int quantidade = estoqueService.buscarQuantidadePorProdutoId(1L);

        assertEquals(12, quantidade);
    }

    @Test
    void deveLancarExceptionAoBuscarEstoqueInexistente() {
        when(estoqueRepository.findByProdutoId(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
            estoqueService.buscarPorProdutoId(1L)
        );

        assertEquals("Estoque não encontrado para o produto id 1", ex.getMessage());
    }
}
