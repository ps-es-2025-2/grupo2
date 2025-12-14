package br.com.geb.api.repository;

import br.com.geb.api.domain.caixa.MovimentacaoCaixa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovimentacaoCaixaRepository extends JpaRepository<MovimentacaoCaixa, Long> {

    //Busca todas as movimentações associadas a um caixa específico
    List<MovimentacaoCaixa> findByCaixaId(Long caixaId);

}

