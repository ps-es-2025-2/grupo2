package br.com.geb.api.domain.evento;

import br.com.geb.api.domain.caixa.Caixa;
import br.com.geb.api.domain.produto.Produto;
import br.com.geb.api.domain.venda.Venda;
import br.com.geb.api.enums.StatusEvento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Nome do evento é obrigatório")
    private String nome;

    @Column(length = 1000)
    private String descricao;

    @Column(nullable = false)
    @NotNull(message = "Data de início é obrigatória")
    private LocalDateTime dataInicio;

    @Column(nullable = false)
    @NotNull(message = "Data de fim é obrigatória")
    private LocalDateTime dataFim;

    @Column(nullable = false)
    @NotBlank(message = "Local é obrigatório")
    private String local;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusEvento status = StatusEvento.PLANEJADO;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Venda> vendas = new ArrayList<>();

    // Relacionamento Evento -> Caixa (múltiplos caixas podem ser abertos para um evento)
    @OneToMany(mappedBy = "evento", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Caixa> caixas = new ArrayList<>();

    // Relacionamento Evento -> Produto (produtos vinculados especificamente ao evento)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "evento_produto",
        joinColumns = @JoinColumn(name = "evento_id"),
        inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    @Builder.Default
    private List<Produto> produtos = new ArrayList<>();

    @PrePersist
    private void prePersist() {
        if (this.status == null) {
            this.status = StatusEvento.PLANEJADO;
        }
    }
}

