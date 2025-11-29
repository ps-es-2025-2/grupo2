package br.com.geb.api.dto;

import br.com.geb.api.enums.TipoMovimentoCaixa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovimentacaoCaixaRequest {
    private Long caixaId;
    private TipoMovimentoCaixa tipo;
    private Double valor;
    private String justificativa;
}

