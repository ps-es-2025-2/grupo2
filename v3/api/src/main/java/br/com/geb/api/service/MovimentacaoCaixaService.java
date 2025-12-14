package br.com.geb.api.service;

import br.com.geb.api.domain.caixa.Caixa;
import br.com.geb.api.domain.caixa.MovimentacaoCaixa;
import br.com.geb.api.exception.ResourceNotFoundException;
import br.com.geb.api.repository.CaixaRepository;
import br.com.geb.api.repository.MovimentacaoCaixaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimentacaoCaixaService {

    private final MovimentacaoCaixaRepository movimentacaoRepository;

    private final CaixaRepository caixaRepository;

    public MovimentacaoCaixaService(MovimentacaoCaixaRepository movimentacaoRepository,
                                    CaixaRepository caixaRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.caixaRepository = caixaRepository;
    }

    @Transactional
    public MovimentacaoCaixa criar(Long caixaId, MovimentacaoCaixa movimentacao) {
        Caixa caixa = caixaRepository.findById(caixaId)
            .orElseThrow(() -> new ResourceNotFoundException("Caixa não encontrado: " + caixaId));

        movimentacao.setCaixa(caixa);

        return movimentacaoRepository.save(movimentacao);
    }

    public MovimentacaoCaixa buscarPorId(Long id) {
        return movimentacaoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Movimentação não encontrada: " + id));
    }

    public List<MovimentacaoCaixa> listarTodos() {
        return movimentacaoRepository.findAll();
    }

    public List<MovimentacaoCaixa> listarPorCaixa(Long caixaId) {
        return movimentacaoRepository.findByCaixaId(caixaId);
    }

    @Transactional
    public void deletar(Long id) {
        if (!movimentacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movimentação não encontrada: " + id);
        }
        movimentacaoRepository.deleteById(id);
    }
}

