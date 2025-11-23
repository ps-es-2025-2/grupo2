package com.geb.gestoreventosbebidas.repository;

import com.geb.gestoreventosbebidas.model.Bebida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Interface que herda todas as operações CRUD básicas
@Repository
public interface BebidaRepository extends JpaRepository<Bebida, Long> {

    // O Spring já fornece métodos como save(), findById(), findAll(), delete(), etc.
    // Você pode adicionar métodos específicos aqui se precisar de consultas personalizadas.
}