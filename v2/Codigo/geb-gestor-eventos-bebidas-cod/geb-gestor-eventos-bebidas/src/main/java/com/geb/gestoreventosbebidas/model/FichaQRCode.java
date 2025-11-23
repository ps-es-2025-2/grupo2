package com.geb.gestoreventosbebidas.model;

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
public class FichaQRCode implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String codigoUnico;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataGeracao;

    // Armazena a representação da imagem do QR Code
    @Lob // Define que o campo pode armazenar um grande volume de dados (Large Object)
    private String qrCodeBase64;

    // Relacionamento 1:1 com Venda (Chave Estrangeira)
    @OneToOne
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;
}