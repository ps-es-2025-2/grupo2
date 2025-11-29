package br.com.geb.api.domain.estoque;

import br.com.geb.api.domain.produto.Produto;
import br.com.geb.api.enums.TipoMovimento;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historico_movimentacao_estoque")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class HistoricoMovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private EstoqueProduto estoque;

    @Enumerated(EnumType.STRING)
    private TipoMovimento tipo;

    private Integer quantidade;

    private LocalDateTime dataHora;

    private String origem;
}

