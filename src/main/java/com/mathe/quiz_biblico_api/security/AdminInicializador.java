package com.mathe.quiz_biblico_api.security;

import com.mathe.quiz_biblico_api.Enum.Role;
import com.mathe.quiz_biblico_api.model.Usuario;
import com.mathe.quiz_biblico_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInicializador implements ApplicationRunner {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final String email;
    private final String senha;
    private final String nome;

    public AdminInicializador(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.email:}") String email,
            @Value("${app.admin.senha:}") String senha,
            @Value("${app.admin.nome:Administrador}") String nome) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.email = email;
        this.senha = senha;
        this.nome = nome;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (email == null || email.isBlank()) {
            return;
        }

        usuarioRepository.findByEmailIgnoreCase(email.trim()).ifPresentOrElse(usuario -> {
            if (usuario.getRole() != Role.ADMIN) {
                usuario.setRole(Role.ADMIN);
                usuarioRepository.save(usuario);
            }
        }, () -> {
            if (senha == null || senha.isBlank()) {
                throw new IllegalStateException("app.admin.senha é obrigatória para criar o administrador.");
            }
            String nomeAdmin = nome == null || nome.isBlank() ? "Administrador" : nome.trim();
            Usuario admin = new Usuario(nomeAdmin, email.trim(), passwordEncoder.encode(senha));
            admin.setRole(Role.ADMIN);
            usuarioRepository.save(admin);
        });
    }
}
