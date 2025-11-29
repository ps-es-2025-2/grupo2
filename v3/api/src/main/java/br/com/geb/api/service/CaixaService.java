package br.com.geb.api.service;

import br.com.geb.api.domain.caixa.Caixa;
import br.com.geb.api.enums.StatusCaixa;
import br.com.geb.api.repository.CaixaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CaixaService {
    private final CaixaRepository repo;

    public CaixaService(CaixaRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Caixa abrirCaixa(Double saldoInicial) {
        Caixa caixa = Caixa.builder()
                .dataAbertura(LocalDateTime.now())
                .saldoInicial(saldoInicial)
                .saldoFinal(0.0)
                .status(StatusCaixa.ABERTO)
                .build();
        return repo.save(caixa);
    }

    @Transactional
    public Caixa fecharCaixa(Long id, Double saldoFinal) {
        Caixa caixa = repo.findById(id).orElseThrow(() -> new RuntimeException("Caixa não encontrada"));
        caixa.setDataFechamento(LocalDateTime.now());
        caixa.setSaldoFinal(saldoFinal);
        caixa.setStatus(StatusCaixa.FECHADO);
        return repo.save(caixa);
    }

    public Optional<Caixa> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public List<Caixa> listarTodos() {
        return repo.findAll();
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}

