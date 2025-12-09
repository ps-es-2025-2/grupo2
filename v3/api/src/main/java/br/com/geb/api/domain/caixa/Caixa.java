package br.com.geb.api.domain.caixa;

import br.com.geb.api.enums.StatusCaixa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "caixa")
public class Caixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataAbertura;

    private LocalDateTime dataFechamento;

    private Double saldoInicial;

    private Double saldoFinal;

    @Enumerated(EnumType.STRING)
    private StatusCaixa status;
}

