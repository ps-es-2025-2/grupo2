package br.com.geb.api.repository;

import br.com.geb.api.domain.venda.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {}
