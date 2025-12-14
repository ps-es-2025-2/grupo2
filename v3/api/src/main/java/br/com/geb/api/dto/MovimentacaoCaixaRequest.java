package br.com.geb.api.dto;

import br.com.geb.api.enums.TipoMovimentoCaixa;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovimentacaoCaixaRequest {

    @NotNull(message = "O ID do caixa é obrigatório.")
    private Long caixaId;

    @NotNull(message = "O tipo de movimentação é obrigatório.")
    private TipoMovimentoCaixa tipo;

    @NotNull(message = "O valor é obrigatório.")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
    private BigDecimal valor;

    @NotBlank(message = "A justificativa é obrigatória.")
    private String justificativa;

}

