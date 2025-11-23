package com.geb.gestoreventosbebidas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Estoque implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantidadeAtual;
    private int quantidadeMinima;

    // Relacionamento N:1 com Bebida (Muitos Estoques para Uma Bebida)
    @ManyToOne
    @JoinColumn(name = "bebida_id", nullable = false)
    private Bebida bebida;

    // Relacionamento 1:1 com Evento (Um Estoque pertence a Um Evento específico)
    @OneToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;
}