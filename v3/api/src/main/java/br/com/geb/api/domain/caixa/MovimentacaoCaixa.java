package br.com.geb.api.domain.caixa;

import br.com.geb.api.enums.TipoMovimentoCaixa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "movimentacao_caixa")
public class MovimentacaoCaixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Caixa caixa;

    @Enumerated(EnumType.STRING)
    private TipoMovimentoCaixa tipo;

    private Double valor;

    private String justificativa;

    private LocalDateTime dataHora;
}

