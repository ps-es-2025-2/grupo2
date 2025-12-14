package br.com.geb.api.domain.venda;

import br.com.geb.api.domain.cliente.Cliente;
import br.com.geb.api.enums.StatusFicha;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "fichas")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class FichaDigital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) //Optional false garante que o relacionamento não pode ser nulo (a ficha precisa de um cliente).
    private Cliente cliente;

    @Column(nullable = false)
    @Builder.Default
    private BigDecimal saldo = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private BigDecimal limite = BigDecimal.ZERO;

    @Column(unique = true, nullable = false, length = 10)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusFicha status;

    @PrePersist
    private void prePersist() {
        if (this.codigo == null || this.codigo.isBlank()) {
            this.codigo = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        }
        if (this.saldo == null) this.saldo = BigDecimal.ZERO;
        if (this.limite == null) this.limite = BigDecimal.ZERO;
        if (this.status == null) this.status = StatusFicha.GERADA;
    }

    /*
        A criacao do metodo prePersist() evita salvar dados incompletos ou inconsistentes,
        deixa o service mais simples, sem logica repetitiva de inicializacao, e deixa as regras da entidade
        dentro dela propria.

        this.codigo → garante que toda ficha tenha um código único mesmo que o service não tenha setado.
        this.saldo e this.limite → inicializa valores financeiros para 0.
        this.status → inicializa o status como GERADA se não foi definido.

        Isso acontece automaticamente antes do repo.save(ficha), logo nao ha necessidade de lembrar de inicializar no service ou controller.
        Assim, evita duplicacao de codigo, garante que nenhuma ficha sera salva com valores nulos ou inconsistentes.
    */
}