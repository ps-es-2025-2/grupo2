package br.com.geb.api.domain.venda;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "validacao_ficha")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class ValidacaoFicha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private FichaDigital ficha;

    @ManyToOne
    private Venda venda;

    private LocalDateTime dataHora;

    private String observacao;

    private String conferenteUsername;
}

