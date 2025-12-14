package br.com.geb.api.controller;

import br.com.geb.api.dto.LoginRequest;
import br.com.geb.api.dto.RegisterRequest;
import br.com.geb.api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController //essa classe é um controlador REST, por isso precisa dessa anotacao
@RequestMapping("/api/auth") //prefixo da rota para todos os endpoints deste controller
public class AuthController {

    /*
        Responsável por gerenciar a autenticação e o registro de usuários na aplicação.
        Login (/login): verifica as credenciais de um usuário existente e retorna um token ou uma sessão válida.
        Registro (/register): cria um novo usuário no sistema com as informações fornecidas.
        Implementa ações críticas de segurança e controle de acesso.

        No front-end, a classe tem uso direto nos fluxos de login e cadastro:

        1. Tela de login: Usuário digita email/username e senha.
        2. Tela de registro: Usuário ou operador preenche formulário de cadastro.

        OBSERVACAO: Alterei a classe para deixar o controller mais limpo e seguir boas praticas,
         como facilitar a manutencao e separar responsabilidades
    */

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /*
        O metodo POST e usado para criar novos dados, enviar informacoes ao servidor e/ou executar acoes que alteram o back.
        Deve ser enviado dados no corpo (body) da requisicao.
    */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        return authService.login(req);
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        return authService.register(req);
    }
}
