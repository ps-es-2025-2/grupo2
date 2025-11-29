package br.com.geb.api.domain.estoque;

import br.com.geb.api.domain.produto.Produto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "estoque")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class EstoqueProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Produto produto;

    private Integer quantidadeAtual;

    private Integer quantidadeMinima;

    private LocalDateTime dataSolicitacao;

    @OneToMany(mappedBy = "estoque", cascade = CascadeType.ALL)
    private List<HistoricoMovimentacaoEstoque> historico;
}