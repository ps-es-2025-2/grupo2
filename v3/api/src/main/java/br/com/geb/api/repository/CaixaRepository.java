package br.com.geb.api.repository;

import br.com.geb.api.domain.caixa.Caixa;
import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.enums.StatusCaixa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CaixaRepository extends JpaRepository<Caixa, Long> {
    Optional<Caixa> findByOperadorAndStatus(Usuario operador, StatusCaixa status);
}

