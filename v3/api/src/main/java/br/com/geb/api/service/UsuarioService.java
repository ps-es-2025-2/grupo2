package br.com.geb.api.service;

import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.dto.RegisterRequest;
import br.com.geb.api.dto.UsuarioResponseDTO;
import br.com.geb.api.exception.ResourceNotFoundException;
import br.com.geb.api.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repo, PasswordEncoder encoder){
        this.repo = repo;
        this.encoder = encoder;
    }

    //Metodo para registrar um novo usuario
    public Usuario registra(RegisterRequest data) {
        if (repo.findByEmail(data.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este email já está em uso");
        }
        /*
            Caso o e-mail digitado ja exista, lanca o erro 409 Conflict:
             conflito de dados — a ação não pode ser concluída enquanto houver duplicidade
        */

        Usuario novoUsuario = Usuario.builder()
                .nome(data.getNome())
                .email(data.getEmail())
                .senha(encoder.encode(data.getSenha()))
                .papel(data.getPapel())
                .build(); //Cria um novo usuario de acordo com as informacoes enviadas

        return repo.save(novoUsuario); //Salva o usuario criado
    }


    public Usuario buscarPorEmail(String email){
        return repo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: email " + email));
    } //Busca o e-mail recebido. Caso nao seja encontrado, lanca o erro

    public boolean checkPassword(Usuario u, String raw){
        return encoder.matches(raw, u.getSenha());
    } //Metodo para verificar a senha

    public List<UsuarioResponseDTO> listarTodos() {
        return repo.findByAtivoTrue().stream()
                .map(UsuarioResponseDTO::new)
                .toList();
    }

    // 2. Buscar por ID
    public Usuario buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: id " + id));
    }

    // 3. Atualizar
    public Usuario atualizar(Long id, br.com.geb.api.dto.UsuarioUpdateDTO dados) {
        Usuario usuario = buscarPorId(id);

        if (dados.nome() != null) usuario.setNome(dados.nome());
        if (dados.email() != null) usuario.setEmail(dados.email());
        if (dados.papel() != null) usuario.setPapel(dados.papel());

        return repo.save(usuario);
    }

    // 4. Deletar
    public void deletar(Long id) {
        Usuario usuario = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: id " + id));

        String emailLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        if (usuario.getEmail().equals(emailLogado)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você não pode desativar seu próprio usuário.");
        }

        usuario.setAtivo(false);
        repo.save(usuario);
    }
}
