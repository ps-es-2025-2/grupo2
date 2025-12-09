package br.com.geb.api.controller;

import br.com.geb.api.dto.LoginRequest;
import br.com.geb.api.dto.RegisterRequest;
import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.exception.ResourceNotFoundException;
import br.com.geb.api.service.UsuarioService;
import br.com.geb.api.config.JwtUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UsuarioService usuarioService, JwtUtils jwtUtils){
        this.usuarioService = usuarioService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req){
        try {
            if (req.getEmail() == null || req.getSenha() == null) {
                return ResponseEntity.badRequest().body("Email e senha são obrigatórios");
            }

            Usuario u = usuarioService.buscarPorEmail(req.getEmail());

            if (!usuarioService.checkPassword(u, req.getSenha())) {
                return ResponseEntity.status(401).body("Senha inválida");
            }

            String token = jwtUtils.generateToken(u.getEmail(), u.getPapel());

            return ResponseEntity.ok(Map.of("token", token));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(401).body("Usuário não encontrado");
        } catch (Exception e) {
            logger.error("Erro durante login", e);
            return ResponseEntity.status(500).body("Erro interno no servidor");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req){
        try {
            Usuario u = usuarioService.register(req);
            String token = jwtUtils.generateToken(u.getEmail(), u.getPapel());

            return ResponseEntity.ok(java.util.Map.of(
                    "message", "Usuário criado com sucesso",
                    "token", token
            ));
        } catch (Exception e) {
            logger.error("Erro no registro", e);

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
