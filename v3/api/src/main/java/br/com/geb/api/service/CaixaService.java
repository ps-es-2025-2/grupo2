package br.com.geb.api.service;

import br.com.geb.api.domain.caixa.Caixa;
import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.enums.StatusCaixa;
import br.com.geb.api.exception.ResourceNotFoundException;
import br.com.geb.api.repository.CaixaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    public Caixa abrirCaixa(BigDecimal saldoInicial, Usuario operador) {
        Caixa caixa = Caixa.builder()
                .operador(operador)
                .dataAbertura(LocalDateTime.now())
                .saldoInicial(saldoInicial)
                .saldoFinal(BigDecimal.ZERO)
                .status(StatusCaixa.ABERTO)
                .build();
        return repo.save(caixa);
    }

    @Transactional
    public Caixa fecharCaixa(Long id, BigDecimal saldoFinal) {
        Caixa caixa = repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Caixa não encontrada com ID: " + id)); //Caso o ID do caixa digitado nao seja encontrado, lanca erro

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
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Caixa não encontrada com ID: " + id);
        } //Verifica se o ID do caixa digitado existe mesmo para ser possivel deletar

        repo.deleteById(id);
    }

    public Optional<Caixa> buscarCaixaAbertoPorOperador(Usuario operador) {
        return repo.findByOperadorAndStatus(operador, StatusCaixa.ABERTO);
    }
}

