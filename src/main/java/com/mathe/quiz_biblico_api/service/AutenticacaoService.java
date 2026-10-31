package com.mathe.quiz_biblico_api.service;

import com.mathe.quiz_biblico_api.Exception.CredenciaisInvalidasException;
import com.mathe.quiz_biblico_api.Exception.UsuarioInativoException;
import com.mathe.quiz_biblico_api.dto.auth.LoginDto;
import com.mathe.quiz_biblico_api.dto.auth.TokenDto;
import com.mathe.quiz_biblico_api.model.Usuario;
import com.mathe.quiz_biblico_api.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AutenticacaoService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public TokenDto login(LoginDto dto) {
        if (dto.email() == null || dto.email().isBlank() || dto.senha() == null || dto.senha().isBlank()) {
            throw new IllegalArgumentException("E-mail e senha são obrigatórios.");
        }

        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(dto.email().trim())
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException();
        }
        if (!usuario.getAtivo()) {
            throw new UsuarioInativoException();
        }
        return jwtService.gerarToken(usuario);
    }
}
