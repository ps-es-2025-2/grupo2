package br.com.geb.api.domain.caixa;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sangria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Sangria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double valor;

    private String justificativa;

    private String operadorUsername;
}

