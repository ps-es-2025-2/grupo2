// Código para colar em src/test/java/EstoqueTest.java

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

class EstoqueTest {

    private Produto cerveja;
    private Evento festaJunina;
    private Estoque estoqueDeCerveja;

    // Este método especial roda ANTES de cada um dos testes abaixo.
    // Ele garante que cada teste comece com um estoque "novo" de 100 cervejas.
    @BeforeEach
    void setUp() {
        // ARRANGE (Arranjar/Preparar): Criamos os objetos necessários para o teste.
        cerveja = new Produto(1, "Cerveja Pilsen", "Garrafa 600ml", 12.50);
        festaJunina = new Evento(101, "Festa Junina da UFLA", new Date(), new Date());
        estoqueDeCerveja = new Estoque(1, cerveja, festaJunina, 100, 10);
    }

    @Test
    void deveDarBaixaDoEstoqueComSucesso() {
        // ACT (Agir): Executamos o método que queremos testar.
        boolean sucesso = estoqueDeCerveja.darBaixaEstoque(20);

        // ASSERT (Afirmar/Verificar): Checamos se o resultado foi o esperado.
        assertTrue(sucesso); // A baixa deve ter retornado 'true'
        assertEquals(80, estoqueDeCerveja.getQuantidadeAtual()); // O estoque deve ter 80 agora
    }

    @Test
    void naoDeveDarBaixaSeEstoqueForInsuficiente() {
        // ACT: Tentamos vender 150 (só temos 100)
        boolean sucesso = estoqueDeCerveja.darBaixaEstoque(150);

        // ASSERT:
        assertFalse(sucesso); // A baixa deve ter retornado 'false'
        assertEquals(100, estoqueDeCerveja.getQuantidadeAtual()); // O estoque não pode ter mudado
    }

    @Test
    void deveReporEstoqueCorretamente() {
        // ACT: Adicionamos 50 cervejas ao estoque.
        estoqueDeCerveja.reporEstoque(50);

        // ASSERT:
        assertEquals(150, estoqueDeCerveja.getQuantidadeAtual()); // O estoque deve ter 150 agora
    }

    @Test
    void deveIndicarAlertaDeEstoqueBaixo() {
        // ACT: Vendemos 95 unidades, sobrando 5 (nosso alerta é 10).
        estoqueDeCerveja.darBaixaEstoque(95);

        // ASSERT:
        assertTrue(estoqueDeCerveja.isAlertaEstoqueBaixo()); // O método de alerta deve retornar 'true'
    }
}