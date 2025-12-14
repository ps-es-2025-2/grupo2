package br.com.geb.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AbrirCaixaRequest {

    @NotNull
    @DecimalMin(value = "0.00", message = "O saldo inicial não pode ser negativo")
    private BigDecimal saldoInicial;

    // Aceita email como string
    @Email(message = "Email do operador inválido")
    private String operadorEmail;

    // Aceita também um objeto operador com email
    private OperadorDTO operador;

    @Getter
    @Setter
    public static class OperadorDTO {
        private Long id;
        private String email;
        private String nome;
    }

    // Método para obter o email do operador de forma flexível
    public String getEmailOperador() {
        if (operadorEmail != null && !operadorEmail.isBlank()) {
            return operadorEmail;
        }
        if (operador != null && operador.getEmail() != null) {
            return operador.getEmail();
        }
        throw new IllegalArgumentException("Email do operador é obrigatório");
    }
}
