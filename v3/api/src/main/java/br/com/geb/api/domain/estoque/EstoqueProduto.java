package br.com.geb.api.domain.estoque;

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

    @OneToOne(optional = false)  //Garante que produto sempre exista
    @JoinColumn(name = "produto_id", unique = true)
    private Produto produto;

    private Integer quantidadeAtual = 0; //Evita null - caso nao tenha, quantidade sera 0

    private Integer quantidadeMinima = 0; //Evita null - caso nao tenha, quantidade sera 0

    private LocalDateTime dataSolicitacao;

    @OneToMany(mappedBy = "estoque", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoMovimentacaoEstoque> historico = new ArrayList<>();
}