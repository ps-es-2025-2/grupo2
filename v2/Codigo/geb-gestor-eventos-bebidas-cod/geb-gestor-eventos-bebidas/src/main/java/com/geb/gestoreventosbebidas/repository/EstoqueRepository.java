package com.geb.gestoreventosbebidas.repository;

import com.geb.gestoreventosbebidas.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    // Para buscar um estoque específico em um evento:
    // Optional<Estoque> findByEventoIdAndBebidaId(Long eventoId, Long bebidaId);
}