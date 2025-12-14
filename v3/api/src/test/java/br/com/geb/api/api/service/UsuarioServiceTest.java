package br.com.geb.api.api.service;

import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.dto.RegisterRequest;
import br.com.geb.api.enums.Papel;
import br.com.geb.api.exception.ResourceNotFoundException;
import br.com.geb.api.repository.UsuarioRepository;
import br.com.geb.api.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    private UsuarioRepository repo;
    private PasswordEncoder encoder;
    private UsuarioService service;

    @BeforeEach
    void setUp() {
        repo = mock(UsuarioRepository.class);
        encoder = mock(PasswordEncoder.class);
        service = new UsuarioService(repo, encoder);
    }

    @Test
    void deveRegistrarUsuarioComSucesso() {
        RegisterRequest req = new RegisterRequest();
        req.setNome("João");
        req.setEmail("joao@test.com");
        req.setSenha("123456");
        req.setPapel(buildPapel());

        when(repo.findByEmail(req.getEmail())).thenReturn(Optional.empty());
        when(encoder.encode(req.getSenha())).thenReturn("hashed123");
        Usuario saved = new Usuario();
        saved.setNome("João");
        saved.setEmail("joao@test.com");
        saved.setSenha("hashed123");
        saved.setPapel(buildPapel());

        when(repo.save(any(Usuario.class))).thenReturn(saved);

        Usuario resultado = service.registra(req);

        assertEquals("João", resultado.getNome());
        assertEquals("joao@test.com", resultado.getEmail());
        assertEquals("hashed123", resultado.getSenha());
        assertEquals(Papel.ROLE_ADMIN, resultado.getPapel());
    }

    @Test
    void deveLancarExcecaoAoRegistrarEmailDuplicado() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("dup@test.com");

        when(repo.findByEmail(req.getEmail())).thenReturn(Optional.of(new Usuario()));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.registra(req));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        assertEquals("Este email já está em uso", ex.getReason());
    }

    @Test
    void deveBuscarUsuarioPorEmailComSucesso() {
        Usuario u = new Usuario();
        u.setEmail("find@test.com");

        when(repo.findByEmail("find@test.com")).thenReturn(Optional.of(u));

        Usuario resultado = service.buscarPorEmail("find@test.com");

        assertEquals("find@test.com", resultado.getEmail());
    }

    @Test
    void deveLancarExcecaoAoBuscarUsuarioInexistente() {
        when(repo.findByEmail("naoexiste@test.com")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
            () -> service.buscarPorEmail("naoexiste@test.com"));

        assertEquals("Usuário não encontrado: email naoexiste@test.com", ex.getMessage());
    }

    @Test
    void deveVerificarSenhaCorreta() {
        Usuario u = new Usuario();
        u.setSenha("hashed123");

        when(encoder.matches("123456", "hashed123")).thenReturn(true);

        assertTrue(service.checkPassword(u, "123456"));
    }

    @Test
    void deveRetornarFalsoParaSenhaIncorreta() {
        Usuario u = new Usuario();
        u.setSenha("hashed123");

        when(encoder.matches("wrong", "hashed123")).thenReturn(false);

        assertFalse(service.checkPassword(u, "wrong"));
    }

    private Papel buildPapel() {
        return Papel.ROLE_ADMIN;
    }
}
