package br.com.geb.api.domain.venda;

import br.com.geb.api.domain.caixa.Caixa;
import br.com.geb.api.domain.cliente.Cliente;
import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.domain.evento.Evento;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendas")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Cliente cliente;

    private LocalDateTime dataHora;

    private BigDecimal valorTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operador_id")
    private Usuario operador;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemVenda> itens = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private FichaDigital ficha;

    @ManyToOne(fetch = FetchType.LAZY)
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY)
    private Caixa caixa;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ValidacaoFicha> validacoes = new ArrayList<>();

    @PrePersist
    private void prePersist() {
        if (this.dataHora == null) {
            this.dataHora = LocalDateTime.now();
        }
        if (this.valorTotal == null) {
            this.valorTotal = BigDecimal.ZERO;
        }
    }

}