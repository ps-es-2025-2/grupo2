package com.geb.gestoreventosbebidas.repository;

import com.geb.gestoreventosbebidas.model.Caixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaixaRepository extends JpaRepository<Caixa, Long> {
    // Você pode adicionar um método aqui, como:
    // Caixa findByAbertoTrue(); // Para encontrar o caixa atualmente aberto
}