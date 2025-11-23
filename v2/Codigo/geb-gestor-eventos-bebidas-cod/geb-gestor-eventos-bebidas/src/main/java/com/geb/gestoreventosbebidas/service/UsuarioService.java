package com.geb.gestoreventosbebidas.service;

import com.geb.gestoreventosbebidas.model.Usuario;
import com.geb.gestoreventosbebidas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

// Nota: Em um projeto real, você usaria o Spring Security e um PasswordEncoder
// para criptografar a senha antes de salvar no banco.
@Service
public class UsuarioService {

    @Autowired private UsuarioRepository usuarioRepository;

    // CU: Cadastrar Usuários
    public Usuario cadastrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Nome de usuário já existe.");
        }

        // Simulação de criptografia (em produção, use BCryptPasswordEncoder)
        // String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        // usuario.setSenha(senhaCriptografada);

        return usuarioRepository.save(usuario);
    }

    // Método auxiliar para buscar por ID (necessário nos controllers)
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    }

    // Método para simular o login/autenticação
    public Usuario autenticar(String username, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            throw new SecurityException("Usuário ou senha inválidos.");
        }

        Usuario usuario = usuarioOpt.get();

        // Simulação de verificação de senha (em produção, use passwordEncoder.matches)
        if (!usuario.getSenha().equals(senha)) {
            throw new SecurityException("Usuário ou senha inválidos.");
        }

        return usuario;
    }
}