package br.com.geb.api.domain.evento;

import br.com.geb.api.domain.venda.Venda;
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

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Venda> vendas = new ArrayList<>();
}

