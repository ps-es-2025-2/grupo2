package br.com.geb.api.domain.caixa;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sangria")
public class Sangria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private String justificativa;

    @Column(nullable = false)
    private String operadorUsername;

    @PrePersist
    @PreUpdate
    private void validar() {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da sangria deve ser maior que zero");
        }
        if (justificativa == null || justificativa.isBlank()) {
            throw new IllegalArgumentException("A justificativa é obrigatória");
        }
        if (operadorUsername == null || operadorUsername.isBlank()) {
            throw new IllegalArgumentException("O operador é obrigatório");
        }
    }
}

