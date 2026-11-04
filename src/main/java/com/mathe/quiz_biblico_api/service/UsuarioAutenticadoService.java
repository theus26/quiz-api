package com.mathe.quiz_biblico_api.service;

import com.mathe.quiz_biblico_api.Exception.UsuarioNotFoundException;
import com.mathe.quiz_biblico_api.model.Usuario;
import com.mathe.quiz_biblico_api.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioAutenticadoService {
    private final UsuarioRepository _usuarioRepository;

    public UsuarioAutenticadoService(UsuarioRepository usuarioRepository) {
        this._usuarioRepository = usuarioRepository;
    }

    public Usuario atual() throws UsuarioNotFoundException {
        Authentication autenticacao = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacao == null || autenticacao.getName() == null || autenticacao.getName().isBlank()) {
            throw new UsuarioNotFoundException();
        }
        return _usuarioRepository.findByEmailIgnoreCase(autenticacao.getName())
                .orElseThrow(UsuarioNotFoundException::new);
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
