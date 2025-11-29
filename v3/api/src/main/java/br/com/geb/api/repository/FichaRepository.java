package br.com.geb.api.repository;

import br.com.geb.api.domain.venda.FichaDigital;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FichaRepository extends JpaRepository<FichaDigital, Long> {
    Optional<FichaDigital> findByCodigo(String codigo);
}
