package com.geb.gestoreventosbebidas.service;

import com.geb.gestoreventosbebidas.enums.EstadoEvento;
import com.geb.gestoreventosbebidas.model.Bebida;
import com.geb.gestoreventosbebidas.model.Estoque;
import com.geb.gestoreventosbebidas.model.Evento;
import com.geb.gestoreventosbebidas.repository.BebidaRepository;
import com.geb.gestoreventosbebidas.repository.EstoqueRepository;
import com.geb.gestoreventosbebidas.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired private EventoRepository eventoRepository;
    @Autowired private EstoqueRepository estoqueRepository;
    @Autowired private BebidaRepository bebidaRepository;

    // CU: Gerenciar Eventos (Criar)
    public Evento criarEvento(Evento evento) {
        evento.setEstado(EstadoEvento.PLANEJADO); // Novo evento sempre começa Planejado
        return eventoRepository.save(evento);
    }

    // Método auxiliar para mudar o estado (CU: Gerenciar Eventos)
    public Evento mudarEstado(Long eventoId, EstadoEvento novoEstado) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado."));

        // Adicionar regras de transição de estado aqui (ex: não pode ir de FINALIZADO para EXECUCAO)
        evento.setEstado(novoEstado);
        return eventoRepository.save(evento);
    }

    /**
     * CU: Definir Estoque Inicial
     * O estoque é definido por bebida, dentro do contexto do evento.
     */
    @Transactional
    public Estoque definirEstoqueInicial(Long eventoId, Long bebidaId, int quantidade, int quantidadeMinima) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado."));

        Bebida bebida = bebidaRepository.findById(bebidaId)
                .orElseThrow(() -> new IllegalArgumentException("Bebida não encontrada."));

        // Verifica se já existe estoque para esta bebida neste evento
        // (Em um projeto complexo, seria necessário um estoque por bebida por evento)
        // Como o seu diagrama mostra Evento -> 1 Estoque, vamos manter a simplicidade,
        // mas um modelo 1:N (Evento tem N Estoques) seria mais realista para várias bebidas.
        // Adaptando a classe de Estoque para o modelo N:1 (Estoque N:1 Bebida, Estoque N:1 Evento)

        Estoque estoque = new Estoque();
        estoque.setEvento(evento);
        estoque.setBebida(bebida);
        estoque.setQuantidadeAtual(quantidade);
        estoque.setQuantidadeMinima(quantidadeMinima);

        return estoqueRepository.save(estoque);
    }
}