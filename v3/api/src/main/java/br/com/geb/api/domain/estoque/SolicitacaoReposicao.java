package br.com.geb.api.domain.estoque;

import br.com.geb.api.enums.StatusReposicao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "solicitacao_reposicao")
public class SolicitacaoReposicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estoque_id")
    private EstoqueProduto estoque;

    private Integer quantidadeSolicitada;

    private LocalDateTime dataSolicitacao;

    private LocalDateTime dataResposta;

    private String observacao;

    @Enumerated(EnumType.STRING)
    private StatusReposicao status;

    @PrePersist
    public void prePersist() {
        if(dataSolicitacao == null) dataSolicitacao = LocalDateTime.now();
        if(status == null) status = StatusReposicao.PENDENTE;
    }
}