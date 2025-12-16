package br.com.geb.api.service;

import br.com.geb.api.domain.cliente.Cliente;
import br.com.geb.api.domain.venda.FichaDigital;
import br.com.geb.api.enums.StatusFicha;
import br.com.geb.api.repository.FichaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class FichaService {

    private final FichaRepository fichaRepository;

    public FichaService(FichaRepository fichaRepository) {
        this.fichaRepository = fichaRepository;
    }

    //Gera uma ficha para um cliente
    public FichaDigital gerarFichaParaCliente(Cliente cliente) {
        return gerarFichaParaCliente(cliente, BigDecimal.ZERO);
    }

    //Gera uma ficha para um cliente com valor específico
    public FichaDigital gerarFichaParaCliente(Cliente cliente, BigDecimal valor) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente obrigatório para gerar ficha");
        }

        if (valor == null) {
            valor = BigDecimal.ZERO;
        }

        FichaDigital ficha = FichaDigital.builder()
            .cliente(cliente)
            .saldo(valor)
            .limite(valor)
            .status(StatusFicha.GERADA)
            .build();

        return fichaRepository.save(ficha);
    }

    public Optional<FichaDigital> findByCodigo(String codigo) {
        return fichaRepository.findByCodigo(codigo);
    }

    public FichaDigital salvar(FichaDigital ficha) {
        if (ficha.getCliente() == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }
        return fichaRepository.save(ficha);
    }

    public List<FichaDigital> listarTodas() {
        return fichaRepository.findAll();
    }

    public void deletar(FichaDigital ficha) {
        fichaRepository.delete(ficha);
    }

    // Busca a última ficha do cliente (qualquer status)
    public FichaDigital buscarFichaDoCliente(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        
        // Retorna a ficha mais recente do cliente
        return fichaRepository.findAll().stream()
            .filter(f -> f.getCliente() != null && 
                         f.getCliente().getId().equals(cliente.getId()))
            .reduce((first, second) -> second) // Pega a última (mais recente)
            .orElse(null);
    }
}
