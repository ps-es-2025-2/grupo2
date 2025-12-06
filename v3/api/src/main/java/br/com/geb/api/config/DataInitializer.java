package br.com.geb.api.config;

import br.com.geb.api.domain.usuario.Usuario;
import br.com.geb.api.enums.Papel;
import br.com.geb.api.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        String adminEmail = "admin@local";
        if (usuarioRepository.findByEmail(adminEmail).isEmpty()) {
            Usuario u = Usuario.builder()
                    .nome("Administrador")
                    .username("admin")
                    .email(adminEmail)
                    .senha(passwordEncoder.encode("admin123"))
                    .papel(Papel.ROLE_ADMIN)
                    .build();

            usuarioRepository.save(u);
            System.out.println("[DataInitializer] Usuário admin criado: " + adminEmail + " / senha: admin123");
        } else {
            System.out.println("[DataInitializer] Usuário admin já existe: " + adminEmail);
        }
    }
}
