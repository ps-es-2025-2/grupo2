package br.com.geb.api.domain.estoque;

import br.com.geb.api.enums.StatusReposicao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacao_reposicao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class SolicitacaoReposicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private EstoqueProduto estoque;

    private Integer quantidadeSolicitada;

    private LocalDateTime dataSolicitacao;

    @Enumerated(EnumType.STRING)
    private StatusReposicao status;
}

