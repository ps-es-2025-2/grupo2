package br.com.geb.api.domain.cliente;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String username;

    private String telefone;

    private String email;

    private String senha;

    @ElementCollection
    private Set<String> papeis;
}