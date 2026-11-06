package com.mathe.quiz_biblico_api.service;

import com.mathe.quiz_biblico_api.Exception.AcessoNegadoException;
import com.mathe.quiz_biblico_api.Exception.UsuarioNotFoundException;
import com.mathe.quiz_biblico_api.dto.PaginacaoDto;
import com.mathe.quiz_biblico_api.dto.usuario.AlterarPapelDto;
import com.mathe.quiz_biblico_api.dto.usuario.AlterarSituacaoDto;
import com.mathe.quiz_biblico_api.dto.usuario.AtualizarUsuarioDto;
import com.mathe.quiz_biblico_api.dto.usuario.CriarUsuarioDto;
import com.mathe.quiz_biblico_api.dto.usuario.UsuarioDto;
import com.mathe.quiz_biblico_api.Enum.Role;
import com.mathe.quiz_biblico_api.model.Usuario;
import com.mathe.quiz_biblico_api.repository.ConjuntoRepository;
import com.mathe.quiz_biblico_api.repository.PerguntaRepository;
import com.mathe.quiz_biblico_api.repository.PontuacaoRepository;
import com.mathe.quiz_biblico_api.repository.RodadaRepository;
import com.mathe.quiz_biblico_api.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class usuarioService {
    private static final int TAMANHO_MAXIMO_PAGINA = 100;

    private final UsuarioRepository _usuarioRepository;
    private final ConjuntoRepository _conjuntoRepository;
    private final PontuacaoRepository _pontuacaoRepository;
    private final PerguntaRepository _perguntaRepository;
    private final RodadaRepository _rodadaRepository;
    private final UsuarioAutenticadoService _usuarioAutenticadoService;
    private final PasswordEncoder passwordEncoder;

    public usuarioService(
            UsuarioRepository usuarioRepository,
            ConjuntoRepository conjuntoRepository,
            PontuacaoRepository pontuacaoRepository,
            PerguntaRepository perguntaRepository,
            RodadaRepository rodadaRepository,
            UsuarioAutenticadoService usuarioAutenticadoService,
            PasswordEncoder passwordEncoder) {
        this._usuarioRepository = usuarioRepository;
        this._conjuntoRepository = conjuntoRepository;
        this._pontuacaoRepository = pontuacaoRepository;
        this._perguntaRepository = perguntaRepository;
        this._rodadaRepository = rodadaRepository;
        this._usuarioAutenticadoService = usuarioAutenticadoService;
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
        exigirProprioOuAdmin(id);
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

    @Transactional(rollbackFor = Exception.class)
    public UsuarioDto alterarPapel(Long id, AlterarPapelDto dto) throws UsuarioNotFoundException {
        exigirAdmin();
        if (dto == null || dto.role() == null) {
            throw new IllegalArgumentException("Informe o perfil.");
        }
        Usuario atual = _usuarioAutenticadoService.atual();
        if (atual.getId() == id) {
            throw new IllegalArgumentException("Você não pode alterar o próprio perfil.");
        }
        Usuario usuario = _usuarioRepository.findById(id).orElseThrow(UsuarioNotFoundException::new);
        if (usuario.getRole() == Role.ADMIN && dto.role() == Role.USER && _usuarioRepository.countByRole(Role.ADMIN) <= 1) {
            throw new IllegalArgumentException("O sistema precisa manter ao menos um administrador.");
        }
        usuario.setRole(dto.role());
        return UsuarioDto.fromEntity(_usuarioRepository.save(usuario));
    }

    @Transactional(rollbackFor = Exception.class)
    public UsuarioDto alterarSituacao(Long id, AlterarSituacaoDto dto) throws UsuarioNotFoundException {
        exigirAdmin();
        if (dto == null) {
            throw new IllegalArgumentException("Informe a situação.");
        }
        Usuario atual = _usuarioAutenticadoService.atual();
        if (atual.getId() == id) {
            throw new IllegalArgumentException("Você não pode alterar a própria situação.");
        }
        Usuario usuario = _usuarioRepository.findById(id).orElseThrow(UsuarioNotFoundException::new);
        if (!dto.ativo() && usuario.getRole() == Role.ADMIN && usuario.getAtivo()
                && _usuarioRepository.countByRoleAndAtivo(Role.ADMIN, true) <= 1) {
            throw new IllegalArgumentException("O sistema precisa manter ao menos um administrador ativo.");
        }
        usuario.setAtivo(dto.ativo());
        return UsuarioDto.fromEntity(_usuarioRepository.save(usuario));
    }

    public UsuarioDto obterUsuarioPorId(Long id) throws UsuarioNotFoundException {
        exigirProprioOuAdmin(id);
        return UsuarioDto.fromEntity(_usuarioRepository.findById(id).orElseThrow(UsuarioNotFoundException::new));
    }

    public PaginacaoDto<UsuarioDto> listarUsuarios(int pagina, int tamanho, Boolean ativo, String busca) {
        exigirAdmin();
        if (pagina < 1) {
            throw new IllegalArgumentException("A página deve ser maior ou igual a 1.");
        }
        if (tamanho < 1 || tamanho > TAMANHO_MAXIMO_PAGINA) {
            throw new IllegalArgumentException("O tamanho da página deve estar entre 1 e " + TAMANHO_MAXIMO_PAGINA + ".");
        }

        Pageable pageable = PageRequest.of(pagina - 1, tamanho, Sort.by(Sort.Order.asc("nome"), Sort.Order.asc("id")));
        String termo = busca == null ? "" : busca.trim();
        Page<Usuario> resultado = _usuarioRepository.pesquisar(ativo, termo, pageable);

        List<UsuarioDto> dados = resultado.getContent().stream()
                .map(UsuarioDto::fromEntity)
                .toList();

        return new PaginacaoDto<>(dados, Math.toIntExact(resultado.getTotalElements()), resultado.getTotalPages());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletarUsuario(Long id) throws UsuarioNotFoundException {
        exigirAdmin();
        Usuario atual = _usuarioAutenticadoService.atual();
        if (atual.getId() == id) {
            throw new IllegalArgumentException("Você não pode excluir a própria conta por aqui.");
        }
        Usuario usuario = _usuarioRepository.findById(id).orElseThrow(UsuarioNotFoundException::new);
        if (usuario.getRole() == Role.ADMIN && _usuarioRepository.countByRole(Role.ADMIN) <= 1) {
            throw new IllegalArgumentException("O sistema precisa manter ao menos um administrador.");
        }
        _conjuntoRepository.deleteByUsuarioId(id);
        _pontuacaoRepository.deleteByUsuarioId(id);
        _perguntaRepository.desvincularAutor(id);
        _perguntaRepository.desvincularRevisor(id);
        _rodadaRepository.desvincularUsuario(id);
        _usuarioRepository.deleteById(id);
    }

    private void exigirAdmin() {
        if (!_usuarioAutenticadoService.adminNoBanco()) {
            throw new AcessoNegadoException();
        }
    }

    private void exigirProprioOuAdmin(Long id) throws UsuarioNotFoundException {
        if (_usuarioAutenticadoService.adminNoBanco()) {
            return;
        }
        if (_usuarioAutenticadoService.atual().getId() != id) {
            throw new AcessoNegadoException();
        }
    }
}
