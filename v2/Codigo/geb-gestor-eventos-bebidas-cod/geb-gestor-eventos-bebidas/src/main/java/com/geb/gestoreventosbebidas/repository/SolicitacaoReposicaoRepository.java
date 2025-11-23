package com.geb.gestoreventosbebidas.repository;

import com.geb.gestoreventosbebidas.model.SolicitacaoReposicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitacaoReposicaoRepository extends JpaRepository<SolicitacaoReposicao, Long> {
}