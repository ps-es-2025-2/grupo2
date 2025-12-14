package br.com.geb.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FecharCaixaRequest {

    @NotNull //O saldo final nao pode ser nulo
    private BigDecimal saldoFinal;

}
