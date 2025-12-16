package br.com.geb.api.domain.caixa;

import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.domain.evento.Evento;
import br.com.geb.api.enums.StatusCaixa;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter //Facilita o acesso GET ao campo usando o Lombok
@Setter //Facilita o acesso SET ao campo usando o Lombok
@NoArgsConstructor
@AllArgsConstructor
@Entity //Anotacao que define a classe como entidade JPA
@Builder //Facilita a criação do objeto
@Table(name = "caixa") //Anotacao que define o nome da tabela (se o nome da classe for igual ao nome da tabela, pode omitir)
public class Caixa {

    @Id //Identifica esse atributo como a chave primária da tabela (o que diferencia)
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Gera o ID automaticamente desse atributo
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operador_id")
    private Usuario operador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id")
    private Evento evento;

    private LocalDateTime dataAbertura;

    private LocalDateTime dataFechamento;

    /*
        Alterei o tipo do saldoInicial e do saldoFinal para BigDecimal, pois e uma boa pratica em sistema financeiros usar o BigDecimal,
        pois tem precisao mais exata e o Double/Float usam ponto flutuante binario que nao representa valores decimais exatamente - o que pode causar erros de arredondamento.
    */

    private BigDecimal saldoInicial;

    private BigDecimal saldoFinal;

    @Enumerated(EnumType.STRING) //Necessário para salvar o enum StatusCaixa como string no banco, evitando problemas com ordem numérica
    private StatusCaixa status;
}

