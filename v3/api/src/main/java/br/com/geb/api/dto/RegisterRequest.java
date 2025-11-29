package br.com.geb.api.dto;

import br.com.geb.api.enums.Papel;
import lombok.Data;

@Data
public class RegisterRequest {
    private String nome;
    private String email;
    private String senha;
    private Papel papel;
}