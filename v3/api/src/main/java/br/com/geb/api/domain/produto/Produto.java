package br.com.geb.api.domain.produto;

import br.com.geb.api.enums.Categoria;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
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