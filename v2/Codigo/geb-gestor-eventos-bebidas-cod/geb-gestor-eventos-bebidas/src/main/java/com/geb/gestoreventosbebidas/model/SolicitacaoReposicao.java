package com.geb.gestoreventosbebidas.model;

import com.geb.gestoreventosbebidas.enums.StatusReposicao;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SolicitacaoReposicao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSolicitacao;

    private int quantidadeSolicitada;

    @Enumerated(EnumType.STRING)
    private StatusReposicao status;

    // Relacionamento N:1 com Evento
    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    // Relacionamento N:1 com Estoque (Qual item específico está sendo reposto)
    @ManyToOne
    @JoinColumn(name = "estoque_id", nullable = false)
    private Estoque estoque;
}