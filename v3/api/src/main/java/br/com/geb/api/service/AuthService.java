package br.com.geb.api.service;

import br.com.geb.api.config.JwtUtils;
import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.dto.LoginRequest;
import br.com.geb.api.dto.RegisterRequest;
import br.com.geb.api.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final UsuarioService usuarioService; //classe responsavel pelas regras de usuário
    private final JwtUtils jwtUtils; //gera e valida tokens JWT

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class); //registra erros e informações importantes no backend

    public AuthService(UsuarioService usuarioService, JwtUtils jwtUtils) {
        this.usuarioService = usuarioService;
        this.jwtUtils = jwtUtils;
    }

    /*
        Metodo abaixo é responsavel pela logica do login
    */
    public ResponseEntity<?> login(LoginRequest req) {
        try {
            if (req.getEmail() == null || req.getSenha() == null) {
                return ResponseEntity.badRequest().body("Email e senha são obrigatórios");
            }
            /*
                Validacao inicial para verificar se foi enviado o email e a senha para verificacao.
                Caso nao seja enviado, sera lancado um BAD REQUEST
            */

            Usuario u = usuarioService.buscarPorEmail(req.getEmail()); //Verificar se o e-mail do usuario existe

            if (!usuarioService.checkPassword(u, req.getSenha())) {
                return ResponseEntity.status(401).body("Senha inválida");
            } //Validacao da senha - caso a senha digitada seja incorreta, retorna como 401 (Unauthorized)

            String token = jwtUtils.generateToken(u.getEmail(), u.getPapel()); //gera o token JWT

            return ResponseEntity.ok(Map.of("token", token)); //retorna o token gerado
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(401).body("Usuário não encontrado");
        } catch (Exception e) {
            logger.error("Erro durante login", e);
            return ResponseEntity.status(500).body("Erro interno no servidor");
        }
    }

    /*
        Metodo abaixo é responsavel pelo registro do usuario
    */
    public ResponseEntity<?> register(RegisterRequest req) {
        try {
            Usuario u = usuarioService.registra(req); //Cria o usuario

            String token = jwtUtils.generateToken(u.getEmail(), u.getPapel()); //Gera o token JWT apos criacao do usuario

            return ResponseEntity.ok(Map.of(
                "message", "Usuário criado com sucesso",
                "token", token
            ));
        } catch (Exception e) { //Caso de um erro no registro, lanca a resposta abaixo
            logger.error("Erro no registro", e); //E "loga" a informacao do motivo do erro
            return ResponseEntity.badRequest().body(e.getMessage());
            //Lanca um bad request (400), pois a requisição foi enviada pelo cliente, mas está incorreta, incompleta ou inválida.
        }
    }
}
