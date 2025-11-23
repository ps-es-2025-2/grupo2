package com.geb.gestoreventosbebidas.repository;

import com.geb.gestoreventosbebidas.model.FichaQRCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FichaQRCodeRepository extends JpaRepository<FichaQRCode, Long> {
    // Para validar a ficha na hora da venda:
    // Optional<FichaQRCode> findByCodigoUnico(String codigoUnico);
}