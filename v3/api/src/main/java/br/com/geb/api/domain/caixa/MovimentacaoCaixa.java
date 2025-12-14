package br.com.geb.api.domain.caixa;

import br.com.geb.api.enums.TipoMovimentoCaixa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
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

    @NotNull(message = "O caixa é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Caixa caixa;

    @NotNull(message = "O tipo de movimentação é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoMovimentoCaixa tipo;

    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor deve ser positivo")
    private BigDecimal valor;

    @NotBlank(message = "A justificativa é obrigatória")
    private String justificativa;

    private LocalDateTime dataHora;

    @PrePersist
    private void prePersist() {
        if (dataHora == null) {
            dataHora = LocalDateTime.now();
        }
    }

}

