package com.geb.gestoreventosbebidas.service;

import com.geb.gestoreventosbebidas.enums.StatusReposicao;
import com.geb.gestoreventosbebidas.model.Estoque;
import com.geb.gestoreventosbebidas.model.SolicitacaoReposicao;
import com.geb.gestoreventosbebidas.repository.EstoqueRepository;
import com.geb.gestoreventosbebidas.repository.SolicitacaoReposicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstoqueService {

    @Autowired private EstoqueRepository estoqueRepository;
    @Autowired private SolicitacaoReposicaoRepository reposicaoRepository;

    // CU: Solicitar Reposição (Feito pelo Conferente)
    public SolicitacaoReposicao solicitarReposicao(Long estoqueId, int quantidade) {
        Estoque estoque = estoqueRepository.findById(estoqueId)
                .orElseThrow(() -> new IllegalArgumentException("Estoque não encontrado."));

        SolicitacaoReposicao solicitacao = new SolicitacaoReposicao();
        solicitacao.setEstoque(estoque);
        solicitacao.setEvento(estoque.getEvento());
        solicitacao.setQuantidadeSolicitada(quantidade);
        solicitacao.setDataSolicitacao(new Date());
        solicitacao.setStatus(StatusReposicao.PENDENTE);

        return reposicaoRepository.save(solicitacao);
    }

    /**
     * CU: Aprovar/Negar Reposição (Feito pelo Administrador)
     * Se aprovado, atualiza o estoque.
     */
    @Transactional
    public SolicitacaoReposicao processarReposicao(Long solicitacaoId, StatusReposicao status) {
        SolicitacaoReposicao solicitacao = reposicaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada."));

        if (solicitacao.getStatus() != StatusReposicao.PENDENTE) {
            throw new IllegalStateException("Solicitação já foi processada.");
        }

        solicitacao.setStatus(status);

        if (status == StatusReposicao.APROVADA) {
            // CU: Atualizar Estoque (inclusão implícita)
            Estoque estoque = solicitacao.getEstoque();
            estoque.setQuantidadeAtual(estoque.getQuantidadeAtual() + solicitacao.getQuantidadeSolicitada());
            estoqueRepository.save(estoque);
        }

        return reposicaoRepository.save(solicitacao);
    }

    // CU: Emitir Alertas de Estoque Mínimo (Feito pelo Sistema)
    public List<Estoque> verificarEstoqueMinimo(Long eventoId) {
        // Busca todos os estoques do evento
        List<Estoque> estoques = estoqueRepository.findAll().stream()
                .filter(e -> e.getEvento().getId().equals(eventoId))
                .collect(Collectors.toList());

        // Filtra e retorna apenas aqueles abaixo do mínimo
        return estoques.stream()
                .filter(e -> e.getQuantidadeAtual() <= e.getQuantidadeMinima())
                .collect(Collectors.toList());
    }

    // CU: Registrar Entrega de Produto (O Conferente registra a entrada no estoque após a aprovação)
    @Transactional
    public Estoque registrarEntrega(Long estoqueId, int quantidadeEntregue) {
        Estoque estoque = estoqueRepository.findById(estoqueId)
                .orElseThrow(() -> new IllegalArgumentException("Estoque não encontrado."));

        // Atualiza a quantidade (pode ser usado como alternativa ao processamento de solicitação, dependendo da regra de negócio)
        estoque.setQuantidadeAtual(estoque.getQuantidadeAtual() + quantidadeEntregue);

        // Lógica: Se houver solicitações PENDENTES relacionadas a este estoque, talvez deva-se atualizar o status delas aqui.

        return estoqueRepository.save(estoque);
    }
}