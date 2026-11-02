package com.mathe.quiz_biblico_api.service;

import com.mathe.quiz_biblico_api.Exception.UsuarioNotFoundException;
import com.mathe.quiz_biblico_api.dto.PaginacaoDto;
import com.mathe.quiz_biblico_api.dto.usuario.AtualizarUsuarioDto;
import com.mathe.quiz_biblico_api.dto.usuario.CriarUsuarioDto;
import com.mathe.quiz_biblico_api.dto.usuario.UsuarioDto;
import com.mathe.quiz_biblico_api.Enum.Role;
import com.mathe.quiz_biblico_api.model.Usuario;
import com.mathe.quiz_biblico_api.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class usuarioService {
    private static final int TAMANHO_MAXIMO_PAGINA = 100;

    private final UsuarioRepository _usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public usuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this._usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioDto CriarUsuario(CriarUsuarioDto dto) {
        if (dto.senha() == null || dto.senha().isBlank()) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }
        if (_usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        Usuario usuario = dto.toEntity();
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setRole(Role.USER);
        return UsuarioDto.fromEntity(_usuarioRepository.save(usuario));
    }

    public UsuarioDto AtualizarUsuario(Long id, AtualizarUsuarioDto dto) throws UsuarioNotFoundException {
        Usuario usuario = _usuarioRepository.findById(id).orElseThrow(UsuarioNotFoundException::new);
        if (_usuarioRepository.existsByEmailAndIdNot(dto.email(), id)) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.senha()));
        }
        return UsuarioDto.fromEntity(_usuarioRepository.save(usuario));
    }

    public UsuarioDto obterUsuarioPorId(Long id) throws UsuarioNotFoundException {
        return UsuarioDto.fromEntity(_usuarioRepository.findById(id).orElseThrow(UsuarioNotFoundException::new));
    }

    public PaginacaoDto<UsuarioDto> listarUsuarios(int pagina, int tamanho, Boolean ativo) {
        if (pagina < 1) {
            throw new IllegalArgumentException("A página deve ser maior ou igual a 1.");
        }
        if (tamanho < 1 || tamanho > TAMANHO_MAXIMO_PAGINA) {
            throw new IllegalArgumentException("O tamanho da página deve estar entre 1 e " + TAMANHO_MAXIMO_PAGINA + ".");
        }

        Pageable pageable = PageRequest.of(pagina - 1, tamanho, Sort.by(Sort.Order.asc("id")));
        Page<Usuario> resultado = ativo == null
                ? _usuarioRepository.findAll(pageable)
                : _usuarioRepository.findByAtivo(ativo, pageable);

        List<UsuarioDto> dados = resultado.getContent().stream()
                .map(UsuarioDto::fromEntity)
                .toList();

        return new PaginacaoDto<>(dados, Math.toIntExact(resultado.getTotalElements()), resultado.getTotalPages());
    }

    public void deletarUsuario(Long id) throws UsuarioNotFoundException {
        if (!_usuarioRepository.existsById(id)) {
            throw new UsuarioNotFoundException();
        }
        _usuarioRepository.deleteById(id);
    }
}
