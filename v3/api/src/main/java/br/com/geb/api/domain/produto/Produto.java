package br.com.geb.api.domain.produto;

import br.com.geb.api.enums.Categoria;
import jakarta.persistence.*;
import lombok.*;

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

    private String nome;

    private Double preco;

    private Double precoUnitario;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private Boolean ativo = true;
}