package com.mathe.quiz_biblico_api.service;

import com.mathe.quiz_biblico_api.Enum.Role;
import com.mathe.quiz_biblico_api.Exception.UsuarioInativoException;
import com.mathe.quiz_biblico_api.Exception.UsuarioNotFoundException;
import com.mathe.quiz_biblico_api.model.Usuario;
import com.mathe.quiz_biblico_api.repository.UsuarioRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioAutenticadoService {
    private final UsuarioRepository _usuarioRepository;

    public UsuarioAutenticadoService(UsuarioRepository usuarioRepository) {
        this._usuarioRepository = usuarioRepository;
    }

    public Usuario atual() throws UsuarioNotFoundException {
        Usuario usuario = atualOpcional().orElseThrow(UsuarioNotFoundException::new);
        if (!usuario.getAtivo()) {
            throw new UsuarioInativoException();
        }
        return usuario;
    }

    public boolean adminNoBanco() {
        return atualOpcional()
                .filter(usuario -> usuario.getAtivo() && usuario.getRole() == Role.ADMIN)
                .isPresent();
    }

    public Optional<Usuario> atualOpcional() {
        Authentication autenticacao = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacao == null
                || !autenticacao.isAuthenticated()
                || autenticacao instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        String email = autenticacao.getName();
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        return _usuarioRepository.findByEmailIgnoreCase(email);
    }

    public boolean admin() {
        Authentication autenticacao = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacao == null) {
            return false;
        }
        return autenticacao.getAuthorities().stream()
                .anyMatch(autoridade -> "ROLE_ADMIN".equals(autoridade.getAuthority()));
    }
}
