package br.com.geb.api.service;

import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.dto.RegisterRequest;
import br.com.geb.api.exception.ResourceNotFoundException;
import br.com.geb.api.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repo, PasswordEncoder encoder){
        this.repo = repo;
        this.encoder = encoder;
    }

    public Usuario register(RegisterRequest data) {
        if (repo.findByEmail(data.getEmail()).isPresent()) {
            throw new RuntimeException("Este email já está em uso.");
        }

        Usuario novoUsuario = Usuario.builder()
                .nome(data.getNome())
                .email(data.getEmail())
                .senha(encoder.encode(data.getSenha()))
                .papel(data.getPapel())
                .build();

        return repo.save(novoUsuario);
    }


    public Usuario buscarPorEmail(String email){
        return repo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: email " + email));
    }

    public boolean checkPassword(Usuario u, String raw){
        return encoder.matches(raw, u.getSenha());
    }
}
