package br.com.geb.api.dto;

import br.com.geb.api.domain.estoque.SolicitacaoReposicao;
import br.com.geb.api.enums.StatusReposicao;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SolicitacaoReposicaoResponseDTO {

    private Long id;
    private Long produtoId;
    private String nomeProduto;
    private Integer quantidadeSolicitada;
    private Integer quantidadeAtualEstoque;
    private final StatusReposicao status;
    private final String observacao;
    private final LocalDateTime dataSolicitacao;
    private final LocalDateTime dataResposta;

    public SolicitacaoReposicaoResponseDTO(SolicitacaoReposicao entity) {
        this.id = entity.getId();
        this.quantidadeSolicitada = entity.getQuantidadeSolicitada();
        this.status = entity.getStatus();
        this.observacao = entity.getObservacao();
        this.dataSolicitacao = entity.getDataSolicitacao();
        this.dataResposta = entity.getDataResposta();

        if (entity.getEstoque() != null) {
            this.quantidadeAtualEstoque = entity.getEstoque().getQuantidadeAtual();

            if (entity.getEstoque().getProduto() != null) {
                this.produtoId = entity.getEstoque().getProduto().getId();
                this.nomeProduto = entity.getEstoque().getProduto().getNome();
            }
        }
    }
}