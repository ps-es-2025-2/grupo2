package br.com.geb.api.api.service;

import br.com.geb.api.config.JwtUtils;
import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.dto.LoginRequest;
import br.com.geb.api.dto.RegisterRequest;
import br.com.geb.api.enums.Papel;
import br.com.geb.api.exception.ResourceNotFoundException;
import br.com.geb.api.service.AuthService;
import br.com.geb.api.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    @Mock
    private UsuarioService usuarioService; // Mock da classe UsuarioService

    @Mock
    private JwtUtils jwtUtils; // Mock da classe JwtUtils

    @InjectMocks
    private AuthService authService; // Classe a ser testada

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa os mocks

        usuario = Usuario.builder()
            .email("usuario@example.com")
            .senha("senha123")
            .papel(Papel.ROLE_ADMIN)
            .build();
    }

    @Test
    void testLoginSuccess() {
        // Simula a busca do usuário no banco de dados
        when(usuarioService.buscarPorEmail("usuario@example.com")).thenReturn(usuario);
        // Simula a validação da senha
        when(usuarioService.checkPassword(any(), any())).thenReturn(true);
        // Simula a geração do token
        when(jwtUtils.generateToken(any(), any())).thenReturn("mocked-jwt-token");

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("usuario@example.com");
        loginRequest.setSenha("senha123");

        ResponseEntity<?> response = authService.login(loginRequest);

        // Verifica se o status de retorno é 200 OK e o token está correto
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String valor = Objects.toString(response.getBody()); // se obj for null, retorna "null"
        assertTrue(valor.contains("mocked-jwt-token"));
    }

    @Test
    void testLoginInvalidPassword() {
        when(usuarioService.buscarPorEmail("usuario@example.com")).thenReturn(usuario);
        when(usuarioService.checkPassword(any(), any())).thenReturn(false); // Simula senha errada

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("usuario@example.com");
        loginRequest.setSenha("senhaErrada");

        ResponseEntity<?> response = authService.login(loginRequest);

        // Verifica se o status de retorno é 401 Unauthorized
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Senha inválida", response.getBody());
    }

    @Test
    void testLoginUserNotFound() {
        when(usuarioService.buscarPorEmail("usuario@example.com")).thenThrow(new ResourceNotFoundException("Usuário não encontrado"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("usuario@example.com");
        loginRequest.setSenha("senha123");

        ResponseEntity<?> response = authService.login(loginRequest);

        // Verifica se o status de retorno é 401 Unauthorized e mensagem de erro
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Usuário não encontrado", response.getBody());
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("usuario@example.com");
        registerRequest.setSenha("senha123");
        registerRequest.setPapel(Papel.ROLE_ADMIN);

        // Simula a criação do usuário
        when(usuarioService.registra(any())).thenReturn(usuario);
        when(jwtUtils.generateToken(any(), any())).thenReturn("mocked-jwt-token");

        ResponseEntity<?> response = authService.register(registerRequest);

        // Verifica se o status de retorno é 200 OK e o token está correto
        assertEquals(HttpStatus.OK, response.getStatusCode());;
        String valor = Objects.toString(response.getBody());
        assertTrue(valor.contains("mocked-jwt-token"));
    }

    @Test
    void testRegisterFail() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("usuario@example.com");
        registerRequest.setSenha("senha123");
        registerRequest.setPapel(Papel.ROLE_ADMIN);

        // Simula erro ao criar usuário
        when(usuarioService.registra(any())).thenThrow(new RuntimeException("Erro ao criar o usuário"));

        ResponseEntity<?> response = authService.register(registerRequest);

        // Verifica se o status de retorno é 400 Bad Request
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro ao criar o usuário", response.getBody());
    }
}
