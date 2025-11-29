package br.com.geb.api.service;

import br.com.geb.api.domain.estoque.SolicitacaoReposicao;
import br.com.geb.api.enums.StatusReposicao;
import br.com.geb.api.repository.SolicitacaoReposicaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitacaoReposicaoService {
    private final SolicitacaoReposicaoRepository repo;

    public SolicitacaoReposicaoService(SolicitacaoReposicaoRepository repo) {
        this.repo = repo;
    }

    public SolicitacaoReposicao criar(SolicitacaoReposicao solicitacao) {
        solicitacao.setDataSolicitacao(LocalDateTime.now());
        solicitacao.setStatus(StatusReposicao.PENDENTE);
        return repo.save(solicitacao);
    }

    public Optional<SolicitacaoReposicao> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public List<SolicitacaoReposicao> listarTodos() {
        return repo.findAll();
    }

    public SolicitacaoReposicao atualizar(Long id, SolicitacaoReposicao solicitacao) {
        solicitacao.setId(id);
        return repo.save(solicitacao);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}

