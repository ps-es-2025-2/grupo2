package br.com.geb.api.domain.evento;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "eventos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

    private String local;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL)
    private List<br.com.geb.api.domain.venda.Venda> vendas;
}

