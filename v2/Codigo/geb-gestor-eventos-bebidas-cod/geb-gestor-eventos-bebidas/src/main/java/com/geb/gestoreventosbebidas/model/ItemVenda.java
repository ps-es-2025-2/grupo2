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
public class ItemVenda implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantidade;
    private double precoUnitario;

    // O método subtotal() do diagrama é feito com um Getter customizado ou um método simples
    public double getSubtotal() {
        return this.quantidade * this.precoUnitario;
    }

    // Relacionamento N:1 com Venda
    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;

    // Relacionamento N:1 com Bebida (para saber qual produto foi vendido)
    @ManyToOne
    @JoinColumn(name = "bebida_id", nullable = false)
    private Bebida bebida;
}