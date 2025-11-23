package com.geb.gestoreventosbebidas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;
import java.util.List;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Venda implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;

    private double valorTotal;

    // Relacionamento 1:N com ItemVenda (compõe)
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenda> itens;

    // Relacionamento 1:1 com FichaQRCode
    @OneToOne(mappedBy = "venda", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FichaQRCode fichaQrCode;
}