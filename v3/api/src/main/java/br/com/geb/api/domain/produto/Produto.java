package br.com.geb.api.domain.produto;

import br.com.geb.api.enums.Categoria;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, scale = 2, precision = 12)
    private BigDecimal preco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;

    @Builder.Default
    private Boolean ativo = true;

    @PrePersist
    public void prePersist() {
        if (ativo == null) ativo = true;
    }

}