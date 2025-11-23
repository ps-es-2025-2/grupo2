package com.geb.gestoreventosbebidas.repository;

import com.geb.gestoreventosbebidas.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
}