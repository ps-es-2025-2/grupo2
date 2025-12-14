package br.com.geb.api.service;

import br.com.geb.api.domain.estoque.EstoqueProduto;
import br.com.geb.api.domain.estoque.SolicitacaoReposicao;
import br.com.geb.api.enums.StatusReposicao;
import br.com.geb.api.exception.ResourceNotFoundException;
import br.com.geb.api.repository.EstoqueRepository;
import br.com.geb.api.repository.SolicitacaoReposicaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitacaoReposicaoService {

    private final SolicitacaoReposicaoRepository repo;
    private final EstoqueRepository estoqueRepository;
    private final EstoqueService estoqueService;

    public SolicitacaoReposicaoService(SolicitacaoReposicaoRepository repo,
                                       EstoqueRepository estoqueRepository,
                                       EstoqueService estoqueService) {
        this.repo = repo;
        this.estoqueRepository = estoqueRepository;
        this.estoqueService = estoqueService;
    }

    @Transactional
    public SolicitacaoReposicao solicitarReposicao(Long produtoId, Integer quantidade, String observacao) {
        EstoqueProduto estoque = estoqueRepository.findByProdutoId(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado para o produto ID: " + produtoId));

        SolicitacaoReposicao solicitacao = SolicitacaoReposicao.builder()
                .estoque(estoque)
                .quantidadeSolicitada(quantidade)
                .observacao(observacao)
                .dataSolicitacao(LocalDateTime.now())
                .status(StatusReposicao.PENDENTE)
                .build();

        return repo.save(solicitacao);
    }

    @Transactional
    public SolicitacaoReposicao aprovar(Long id) {
        SolicitacaoReposicao solicitacao = buscarPorId(id);

        validarStatusPendente(solicitacao);

        solicitacao.setStatus(StatusReposicao.CONCLUIDA);
        solicitacao.setDataResposta(LocalDateTime.now());

        estoqueService.incrementar(
                solicitacao.getEstoque().getProduto().getId(),
                solicitacao.getQuantidadeSolicitada(),
                "REPOSICAO_APROVADA_ID_" + solicitacao.getId()
        );

        return repo.save(solicitacao);
    }

    @Transactional
    public SolicitacaoReposicao rejeitar(Long id) {
        SolicitacaoReposicao solicitacao = buscarPorId(id);

        validarStatusPendente(solicitacao);

        solicitacao.setStatus(StatusReposicao.REJEITADA);
        solicitacao.setDataResposta(LocalDateTime.now());

        return repo.save(solicitacao);
    }

    public List<SolicitacaoReposicao> listarTodos() {
        return repo.findAll();
    }

    public List<SolicitacaoReposicao> listarPorStatus(StatusReposicao status) {
        return repo.findByStatus(status);
    }

    public SolicitacaoReposicao buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada: " + id));
    }

    private void validarStatusPendente(SolicitacaoReposicao s) {
        if (s.getStatus() != StatusReposicao.PENDENTE) {
            throw new IllegalStateException("Esta solicitação já foi processada (Status: " + s.getStatus() + ")");
        }
    }
}