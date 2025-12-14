package br.com.geb.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SolicitacaoRequest(
        @NotNull Long produtoId,
        @NotNull @Min(1) Integer quantidade,
        String observacao
) {}