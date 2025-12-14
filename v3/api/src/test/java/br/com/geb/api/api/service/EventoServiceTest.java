package br.com.geb.api.api.service;

import br.com.geb.api.domain.evento.Evento;
import br.com.geb.api.exception.ResourceNotFoundException;
import br.com.geb.api.repository.EventoRepository;
import br.com.geb.api.service.EventoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventoServiceTest {

    private EventoRepository repo;
    private EventoService service;

    @BeforeEach
    void setUp() {
        repo = mock(EventoRepository.class);
        service = new EventoService(repo);
    }

    @Test
    void deveCriarEventoComSucesso() {
        Evento evento = Evento.builder()
            .nome("Festa")
            .descricao("Festa de aniversário")
            .local("Salão")
            .dataInicio(LocalDateTime.now().plusDays(1))
            .dataFim(LocalDateTime.now().plusDays(2))
            .build();

        when(repo.save(evento)).thenReturn(evento);

        Evento resultado = service.criar(evento);

        assertEquals("Festa", resultado.getNome());
        verify(repo, times(1)).save(evento);
    }

    @Test
    void deveLancarExceptionSeDataFimAntesDataInicio() {
        Evento evento = Evento.builder()
            .nome("Evento inválido")
            .dataInicio(LocalDateTime.now().plusDays(2))
            .dataFim(LocalDateTime.now().plusDays(1))
            .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.criar(evento));
        assertEquals("A data de fim não pode ser antes da data de início", ex.getMessage());
    }

    @Test
    void deveLancarExceptionSeDataInicioNoPassado() {
        Evento evento = Evento.builder()
            .nome("Evento passado")
            .dataInicio(LocalDateTime.now().minusDays(1))
            .dataFim(LocalDateTime.now().plusDays(1))
            .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.criar(evento));
        assertEquals("A data de início do evento não pode ser no passado", ex.getMessage());
    }

    @Test
    void deveBuscarEventoPorIdComSucesso() {
        Evento evento = new Evento();
        evento.setId(1L);

        when(repo.findById(1L)).thenReturn(Optional.of(evento));

        Evento resultado = service.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void deveLancarExceptionSeEventoNaoExistir() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(1L));
        assertEquals("Evento não encontrado com ID: 1", ex.getMessage());
    }

    @Test
    void deveListarTodosEventos() {
        Evento e1 = new Evento();
        Evento e2 = new Evento();

        when(repo.findAll()).thenReturn(Arrays.asList(e1, e2));

        List<Evento> resultado = service.listarTodos();

        assertEquals(2, resultado.size());
    }

    @Test
    void deveAtualizarEventoComSucesso() {
        Evento existente = Evento.builder()
            .id(1L)
            .nome("Antigo")
            .dataInicio(LocalDateTime.now().plusDays(1))
            .dataFim(LocalDateTime.now().plusDays(2))
            .build();

        Evento atualizado = Evento.builder()
            .nome("Novo")
            .dataInicio(LocalDateTime.now().plusDays(3))
            .dataFim(LocalDateTime.now().plusDays(4))
            .build();

        when(repo.findById(1L)).thenReturn(Optional.of(existente));
        when(repo.save(any(Evento.class))).thenAnswer(i -> i.getArgument(0));

        Evento resultado = service.atualizar(1L, atualizado);

        assertEquals("Novo", resultado.getNome());
        assertEquals(atualizado.getDataInicio(), resultado.getDataInicio());
    }

    @Test
    void deveDeletarEventoComSucesso() {
        Evento evento = new Evento();
        evento.setId(1L);

        when(repo.findById(1L)).thenReturn(Optional.of(evento));
        doNothing().when(repo).delete(evento);

        service.deletar(1L);

        verify(repo, times(1)).delete(evento);
    }
}