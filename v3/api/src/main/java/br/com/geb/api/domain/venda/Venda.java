package br.com.geb.api.domain.venda;

import br.com.geb.api.domain.cliente.Cliente;
import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.domain.evento.Evento;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vendas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cliente cliente;

    private LocalDateTime dataHora;

    private Double valorTotal;

    @ManyToOne
    @JoinColumn(name = "operador_id")
    private Usuario operador;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    private List<ItemVenda> itens;

    @OneToOne
    private FichaDigital ficha;

    @ManyToOne
    private Evento evento;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    private List<ValidacaoFicha> validacoes;
}