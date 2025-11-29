package br.com.geb.api.service;

import br.com.geb.api.domain.venda.FichaDigital;
import br.com.geb.api.repository.FichaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class FichaService {
    private final FichaRepository repo;
    public FichaService(FichaRepository repo){ this.repo = repo; }

    public FichaDigital gerarFicha(){
        FichaDigital f = FichaDigital.builder()
                .codigo(UUID.randomUUID().toString().replace("-","").substring(0,10))
                .status(br.com.geb.api.enums.StatusFicha.GERADA)
                .saldo(0.0)
                .build();
        return repo.save(f);
    }

    public Optional<FichaDigital> findByCodigo(String codigo){
        return repo.findByCodigo(codigo);
    }

    public FichaDigital salvar(FichaDigital f){ return repo.save(f); }
}
