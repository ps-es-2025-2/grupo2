package br.com.geb.api.domain.venda;

import br.com.geb.api.domain.cliente.Cliente;
import br.com.geb.api.enums.StatusFicha;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fichas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class FichaDigital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cliente cliente;

    private Double saldo;

    private Double limite;

    @Column(unique = true)
    private String codigo;

    @Enumerated(EnumType.STRING)
    private StatusFicha status;
}