package br.com.geb.api.repository;

import br.com.geb.api.domain.caixa.Caixa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaixaRepository extends JpaRepository<Caixa, Long> {}

