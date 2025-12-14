package br.com.geb.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AtualizarEstoqueRequest(
        @NotNull(message = "A quantidade atual é obrigatória")
        @Min(value = 0, message = "A quantidade não pode ser negativa")
        Integer quantidadeAtual,

        @NotNull(message = "A quantidade mínima é obrigatória")
        @Min(value = 0, message = "A quantidade mínima não pode ser negativa")
        Integer quantidadeMinima
) {}