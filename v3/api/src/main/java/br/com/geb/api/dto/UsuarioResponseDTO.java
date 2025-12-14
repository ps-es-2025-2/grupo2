package br.com.geb.api.dto;

import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.enums.Papel;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String username,
        Papel papel
) {
    public UsuarioResponseDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getUsername(),
                usuario.getPapel()
        );
    }
}