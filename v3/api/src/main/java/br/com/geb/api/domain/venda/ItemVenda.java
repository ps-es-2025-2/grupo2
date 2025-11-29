package br.com.geb.api.domain.venda;

import br.com.geb.api.domain.produto.Produto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "itens_venda")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Venda venda;

    @ManyToOne
    private Produto produto;

    private Integer quantidade;

    private Double subtotal;
}