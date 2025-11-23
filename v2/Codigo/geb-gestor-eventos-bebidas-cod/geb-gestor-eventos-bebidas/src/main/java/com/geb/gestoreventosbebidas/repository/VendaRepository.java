package com.geb.gestoreventosbebidas.repository;

import com.geb.gestoreventosbebidas.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
}