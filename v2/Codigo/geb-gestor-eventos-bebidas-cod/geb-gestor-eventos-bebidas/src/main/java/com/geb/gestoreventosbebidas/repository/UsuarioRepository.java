package com.geb.gestoreventosbebidas.repository;

import com.geb.gestoreventosbebidas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método essencial para autenticação/login:
    // Optional<Usuario> findByUsername(String username);
}