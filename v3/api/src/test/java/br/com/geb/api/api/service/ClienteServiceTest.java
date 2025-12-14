package br.com.geb.api.api.service;

import br.com.geb.api.domain.cliente.Cliente;
import br.com.geb.api.dto.ClienteRequest;
import br.com.geb.api.repository.ClienteRepository;
import br.com.geb.api.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    private ClienteRepository clienteRepository;

    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        clienteRepository = mock(ClienteRepository.class);
        clienteService = new ClienteService(clienteRepository);
    }

    @Test
    void deveBuscarClientePorIdExistente() {
        Cliente cliente = Cliente.builder().id(1L).nome("João").build();
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.buscarPorId(1L);

        assertEquals(cliente, resultado);
        verify(clienteRepository).findById(1L);
    }

    @Test
    void deveLancarExceptionAoBuscarClienteInexistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            clienteService.buscarPorId(1L)
        );

        assertEquals("Cliente não encontrado", exception.getMessage());
    }

    @Test
    void deveCadastrarCliente() {
        ClienteRequest request = new ClienteRequest();
        request.setNome("Maria");
        request.setEmail("maria@email.com");
        request.setTelefone("123456789");
        request.setUsername("maria123");

        Cliente clienteSalvo = Cliente.builder()
            .id(1L)
            .nome(request.getNome())
            .email(request.getEmail())
            .telefone(request.getTelefone())
            .username(request.getUsername())
            .build();

        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);

        Cliente resultado = clienteService.cadastrar(request);

        assertEquals(clienteSalvo, resultado);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void deveListarTodosClientes() {
        Cliente c1 = Cliente.builder().id(1L).nome("João").build();
        Cliente c2 = Cliente.builder().id(2L).nome("Maria").build();
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Cliente> resultado = clienteService.listarTodos();

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(c1));
        assertTrue(resultado.contains(c2));
    }

    @Test
    void deveAtualizarClienteExistente() {
        ClienteRequest request = new ClienteRequest();
        request.setNome("Maria Atualizada");
        request.setEmail("maria@novoemail.com");
        request.setTelefone("987654321");
        request.setUsername("mariaAtual");

        Cliente clienteExistente = Cliente.builder()
            .id(1L)
            .nome("Maria")
            .email("maria@email.com")
            .telefone("123456789")
            .username("maria123")
            .build();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cliente resultado = clienteService.atualizar(1L, request);

        assertEquals(request.getNome(), resultado.getNome());
        assertEquals(request.getEmail(), resultado.getEmail());
        assertEquals(request.getTelefone(), resultado.getTelefone());
        assertEquals(request.getUsername(), resultado.getUsername());

        verify(clienteRepository).findById(1L);
        verify(clienteRepository).save(clienteExistente);
    }

    @Test
    void deveLancarExceptionAoAtualizarClienteInexistente() {
        ClienteRequest request = new ClienteRequest();
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            clienteService.atualizar(1L, request)
        );

        assertEquals("Cliente não encontrado", exception.getMessage());
    }

    @Test
    void deveDeletarClienteExistente() {
        Cliente clienteExistente = Cliente.builder().id(1L).build();
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));
        doNothing().when(clienteRepository).delete(clienteExistente);

        clienteService.deletar(1L);

        verify(clienteRepository).findById(1L);
        verify(clienteRepository).delete(clienteExistente);
    }

    @Test
    void deveLancarExceptionAoDeletarClienteInexistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            clienteService.deletar(1L)
        );

        assertEquals("Cliente não encontrado", exception.getMessage());
    }
}