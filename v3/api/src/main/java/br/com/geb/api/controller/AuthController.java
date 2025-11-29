package br.com.geb.api.controller;

import br.com.geb.api.dto.LoginRequest;
import br.com.geb.api.dto.RegisterRequest;
import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.service.UsuarioService;
import br.com.geb.api.config.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UsuarioService usuarioService;
    private final JwtUtils jwtUtils;

    public AuthController(UsuarioService usuarioService, JwtUtils jwtUtils){
        this.usuarioService = usuarioService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req){
        Usuario u = usuarioService.findByEmail(req.getEmail()).orElse(null);

        if (u == null) return ResponseEntity.status(401).body("Usuário não encontrado");

        if (!usuarioService.checkPassword(u, req.getSenha())) return ResponseEntity.status(401).body("Senha inválida");

        String token = jwtUtils.generateToken(u.getEmail(), u.getPapel());

        return ResponseEntity.ok(java.util.Map.of("token", token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req){
        try {
            Usuario u = usuarioService.register(req);

            String token = jwtUtils.generateToken(u.getEmail(), u.getPapel());

            return ResponseEntity.ok(java.util.Map.of(
                    "message", "Usuário criado com sucesso",
                    "token", token
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
