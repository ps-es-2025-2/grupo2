package br.com.geb.api.domain.estoque;

import br.com.geb.api.domain.evento.Evento;
import br.com.geb.api.domain.produto.Produto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "estoque")
public class EstoqueProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", unique = true)
    private Produto produto;

    @Builder.Default
    private Integer quantidadeAtual = 0;

    @Builder.Default
    private Integer quantidadeMinima = 0;

    private LocalDateTime dataSolicitacao;

    @OneToMany(mappedBy = "estoque", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HistoricoMovimentacaoEstoque> historico = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = true)
    private Evento evento;
}