package com.geb.gestoreventosbebidas.model;

import com.geb.gestoreventosbebidas.enums.EstadoEvento;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Evento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInicio;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFim;

    @Enumerated(EnumType.STRING)
    private EstadoEvento estado;

    // Relacionamento 1:1 com Estoque (O diagrama sugere composição, mapeado como OneToOne aqui)
    // Usamos 'mappedBy' para indicar que o campo 'evento' é quem gerencia o relacionamento na classe Estoque.
    @OneToOne(mappedBy = "evento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Estoque estoque;

    // Relacionamento 1:N com SolicitacaoReposicao
    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SolicitacaoReposicao> solicitacoes;
}