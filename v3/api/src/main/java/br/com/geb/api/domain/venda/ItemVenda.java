package br.com.geb.api.domain.venda;

import br.com.geb.api.domain.produto.Produto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "itens_venda")
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

    private BigDecimal subtotal;

    public void calcularSubtotal() {
        if (produto != null && produto.getPreco() != null && quantidade != null) {
            this.subtotal = produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
        } else {
            this.subtotal = BigDecimal.ZERO;
        }
    }
}