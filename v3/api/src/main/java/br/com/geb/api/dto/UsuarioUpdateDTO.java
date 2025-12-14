package br.com.geb.api.dto;

import br.com.geb.api.enums.Papel;

public record UsuarioUpdateDTO(
        String nome,
        String email,
        Papel papel
) {}