package com.mathe.quiz_biblico_api.service;

import com.mathe.quiz_biblico_api.Enum.FiltroTestamento;
import com.mathe.quiz_biblico_api.Enum.Modalidade;
import com.mathe.quiz_biblico_api.Enum.Nivel;
import com.mathe.quiz_biblico_api.Enum.StatusPergunta;
import com.mathe.quiz_biblico_api.Enum.Testamento;
import com.mathe.quiz_biblico_api.Exception.AcessoNegadoException;
import com.mathe.quiz_biblico_api.Exception.PerguntaNotFoundException;
import com.mathe.quiz_biblico_api.Exception.UsuarioNotFoundException;
import com.mathe.quiz_biblico_api.dto.PaginacaoDto;
import com.mathe.quiz_biblico_api.dto.alternativa.AlternativaAdminDto;
import com.mathe.quiz_biblico_api.dto.alternativa.AlternativaDto;
import com.mathe.quiz_biblico_api.dto.pergunta.AtualizarPerguntaDto;
import com.mathe.quiz_biblico_api.dto.pergunta.CriarPerguntaDto;
import com.mathe.quiz_biblico_api.dto.pergunta.PainelModeracaoDto;
import com.mathe.quiz_biblico_api.dto.pergunta.PerguntaAdminDto;
import com.mathe.quiz_biblico_api.dto.pergunta.PerguntaComAlternativasDto;
import com.mathe.quiz_biblico_api.dto.pergunta.PerguntaDetalheDto;
import com.mathe.quiz_biblico_api.dto.pergunta.PerguntaResumoDto;
import com.mathe.quiz_biblico_api.model.Pergunta;
import com.mathe.quiz_biblico_api.model.Usuario;
import com.mathe.quiz_biblico_api.repository.ConjuntoRepository;
import com.mathe.quiz_biblico_api.repository.PerguntaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class PerguntaService {
    private static final int QUANTIDADE_DIFICIL = 5;
    private static final int QUANTIDADE_MEDIO = 5;
    private static final int QUANTIDADE_FACIL = 7;
    private static final int TAMANHO_MAXIMO_PAGINA = 100;
    private static final int LIMITE_MODERACAO = 80;

    private final PerguntaRepository _perguntaRepository;
    private final AlternativaService _alternativaService;
    private final ConjuntoRepository _conjuntoRepository;
    private final UsuarioAutenticadoService _usuarioAutenticadoService;

    public PerguntaService(
            PerguntaRepository perguntaRepository,
            AlternativaService alternativaService,
            ConjuntoRepository conjuntoRepository,
            UsuarioAutenticadoService usuarioAutenticadoService) {
        this._perguntaRepository = perguntaRepository;
        this._alternativaService = alternativaService;
        this._conjuntoRepository = conjuntoRepository;
        this._usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @Transactional(rollbackFor = Exception.class)
    public PerguntaAdminDto criar(CriarPerguntaDto dto) throws UsuarioNotFoundException {
        exigirAdmin();
        Usuario autor = _usuarioAutenticadoService.atual();
        Pergunta pergunta = novaPergunta(dto, autor, StatusPergunta.APROVADA, false);
        pergunta.setRevisor(autor);
        pergunta.setRevisadaEm(Instant.now());
        return paraGestao(salvar(pergunta, dto));
    }

    @Transactional(rollbackFor = Exception.class)
    public PerguntaDetalheDto enviar(CriarPerguntaDto dto) throws UsuarioNotFoundException {
        Usuario autor = _usuarioAutenticadoService.atual();
        Pergunta pergunta = novaPergunta(dto, autor, StatusPergunta.PENDENTE, true);
        return detalhe(salvar(pergunta, dto), autor.getId(), true);
    }

    @Transactional(rollbackFor = Exception.class)
    public PerguntaAdminDto atualizar(Long id, AtualizarPerguntaDto dto) throws PerguntaNotFoundException, UsuarioNotFoundException {
        exigirAdmin();
        validarPergunta(dto.enunciado(), dto.testamento(), dto.nivel());
        Pergunta pergunta = _perguntaRepository.findById(id).orElseThrow(PerguntaNotFoundException::new);
        String enunciado = dto.enunciado().trim();
        if (_perguntaRepository.contarPorEnunciadoExceto(enunciado, id) > 0) {
            throw new IllegalArgumentException("Já existe uma pergunta com este enunciado.");
        }
        pergunta.setEnunciado(enunciado);
        pergunta.setTestamento(dto.testamento());
        pergunta.setNivel(dto.nivel());
        pergunta.setReferencia(limpar(dto.referencia(), 120, "A referência"));
        if (dto.categoria() != null) {
            pergunta.setCategoria(limpar(dto.categoria(), 80, "A categoria"));
        }
        if (dto.explicacao() != null) {
            pergunta.setExplicacao(limpar(dto.explicacao(), 500, "A explicação"));
        }
        if (dto.alternativas() != null) {
            _alternativaService.substituir(pergunta, dto.alternativas());
        }
        return paraGestao(_perguntaRepository.save(pergunta));
    }

    @Transactional(readOnly = true)
    public PerguntaComAlternativasDto obter(Long id) throws PerguntaNotFoundException {
        Pergunta pergunta = _perguntaRepository.findById(id).orElseThrow(PerguntaNotFoundException::new);
        if (!disponivelParaJogo(pergunta) && !_usuarioAutenticadoService.adminNoBanco()) {
            throw new PerguntaNotFoundException();
        }
        return PerguntaComAlternativasDto.fromEntity(pergunta, _alternativaService.listarPorPergunta(id));
    }

    @Transactional(readOnly = true)
    public List<PerguntaComAlternativasDto> listar(FiltroTestamento filtro) {
        return sortearParaPartida(filtro, Modalidade.CLASSICO, null);
    }

    @Transactional(readOnly = true)
    public List<PerguntaComAlternativasDto> sortearParaPartida(
            FiltroTestamento filtro,
            Modalidade modalidade,
            Nivel nivel) {
        Testamento testamento = filtro == null || filtro == FiltroTestamento.AMBOS
                ? null
                : Testamento.valueOf(filtro.name());
        Collection<Long> excluirIds = _usuarioAutenticadoService.atualOpcional()
                .filter(Usuario::getAtivo)
                .map(usuario -> perguntasJaVistas(usuario.getId()))
                .orElse(List.of(-1L));
        List<Pergunta> perguntas = montar(modalidade == null ? Modalidade.CLASSICO : modalidade, nivel, testamento, excluirIds);
        Collections.shuffle(perguntas);
        List<Long> ids = perguntas.stream().map(Pergunta::getId).toList();
        Map<Long, List<AlternativaDto>> alternativas = _alternativaService.listarPorPerguntas(ids);
        return perguntas.stream()
                .map(pergunta -> PerguntaComAlternativasDto.fromEntity(
                        pergunta,
                        alternativas.getOrDefault(pergunta.getId(), List.of())
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean conferir(Long perguntaId, Long alternativaId) {
        return _alternativaService.conferir(perguntaId, alternativaId);
    }

    @Transactional(readOnly = true)
    public AlternativaDto revelarCorreta(Long perguntaId) throws PerguntaNotFoundException {
        if (!_perguntaRepository.existsById(perguntaId)) {
            throw new PerguntaNotFoundException();
        }
        return _alternativaService.revelarCorreta(perguntaId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletar(Long id) throws PerguntaNotFoundException, UsuarioNotFoundException {
        exigirAdmin();
        if (!_perguntaRepository.existsById(id)) {
            throw new PerguntaNotFoundException();
        }
        _conjuntoRepository.deleteByPerguntaId(id);
        _alternativaService.deletarPorPergunta(id);
        _perguntaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PaginacaoDto<PerguntaAdminDto> gerenciar(int pagina, int tamanho) throws UsuarioNotFoundException {
        exigirAdmin();
        if (pagina < 1) {
            throw new IllegalArgumentException("A página deve ser maior ou igual a 1.");
        }
        if (tamanho < 1 || tamanho > TAMANHO_MAXIMO_PAGINA) {
            throw new IllegalArgumentException("O tamanho da página deve estar entre 1 e " + TAMANHO_MAXIMO_PAGINA + ".");
        }
        Pageable pageable = PageRequest.of(pagina - 1, tamanho, Sort.by(Sort.Order.asc("id")));
        Page<Pergunta> resultado = _perguntaRepository.findAll(pageable);
        return new PaginacaoDto<>(mapearGestao(resultado.getContent()), Math.toIntExact(resultado.getTotalElements()), resultado.getTotalPages());
    }

    @Transactional(readOnly = true)
    public List<PerguntaResumoDto> minhas() throws UsuarioNotFoundException {
        Usuario autor = _usuarioAutenticadoService.atual();
        return _perguntaRepository.findByAutorId(autor.getId()).stream()
                .map(this::resumo)
                .toList();
    }

    @Transactional(readOnly = true)
    public PerguntaDetalheDto minha(Long id) throws PerguntaNotFoundException, UsuarioNotFoundException {
        Usuario autor = _usuarioAutenticadoService.atual();
        Pergunta pergunta = _perguntaRepository.findById(id).orElseThrow(PerguntaNotFoundException::new);
        if (pergunta.getAutor() == null || pergunta.getAutor().getId() != autor.getId()) {
            throw new AcessoNegadoException();
        }
        return detalhe(pergunta, autor.getId(), false);
    }

    @Transactional(readOnly = true)
    public PainelModeracaoDto moderar(StatusPergunta status, String busca) throws UsuarioNotFoundException {
        exigirAdmin();
        String termo = busca == null ? "" : busca.trim().toLowerCase(Locale.ROOT);
        List<PerguntaResumoDto> perguntas = _perguntaRepository
                .listarParaModeracao(status, PageRequest.of(0, LIMITE_MODERACAO))
                .stream()
                .filter(pergunta -> termo.isEmpty() || corresponde(pergunta, termo))
                .map(this::resumo)
                .toList();
        long aprovadas = _perguntaRepository.countByStatus(StatusPergunta.APROVADA) + _perguntaRepository.countByStatusIsNull();
        return new PainelModeracaoDto(
                _perguntaRepository.countByStatus(StatusPergunta.PENDENTE),
                aprovadas,
                _perguntaRepository.countByStatus(StatusPergunta.REJEITADA),
                perguntas
        );
    }

    @Transactional(readOnly = true)
    public PerguntaDetalheDto detalheModeracao(Long id) throws PerguntaNotFoundException, UsuarioNotFoundException {
        exigirAdmin();
        Pergunta pergunta = _perguntaRepository.findById(id).orElseThrow(PerguntaNotFoundException::new);
        return detalhe(pergunta, _usuarioAutenticadoService.atual().getId(), true);
    }

    @Transactional(rollbackFor = Exception.class)
    public PerguntaDetalheDto aprovar(Long id) throws PerguntaNotFoundException, UsuarioNotFoundException {
        Usuario revisor = exigirAdmin();
        Pergunta pergunta = _perguntaRepository.findById(id).orElseThrow(PerguntaNotFoundException::new);
        pergunta.setStatus(StatusPergunta.APROVADA);
        pergunta.setAtiva(true);
        pergunta.setMotivoRejeicao(null);
        pergunta.setRevisor(revisor);
        pergunta.setRevisadaEm(Instant.now());
        return detalhe(_perguntaRepository.save(pergunta), revisor.getId(), true);
    }

    @Transactional(rollbackFor = Exception.class)
    public PerguntaDetalheDto rejeitar(Long id, String motivo) throws PerguntaNotFoundException, UsuarioNotFoundException {
        Usuario revisor = exigirAdmin();
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("Informe o motivo da rejeição.");
        }
        String texto = motivo.trim();
        if (texto.length() > 500) {
            throw new IllegalArgumentException("O motivo deve ter no máximo 500 caracteres.");
        }
        Pergunta pergunta = _perguntaRepository.findById(id).orElseThrow(PerguntaNotFoundException::new);
        pergunta.setStatus(StatusPergunta.REJEITADA);
        pergunta.setMotivoRejeicao(texto);
        pergunta.setRevisor(revisor);
        pergunta.setRevisadaEm(Instant.now());
        return detalhe(_perguntaRepository.save(pergunta), revisor.getId(), true);
    }

    private Pergunta salvar(Pergunta pergunta, CriarPerguntaDto dto) {
        Pergunta salva = _perguntaRepository.save(pergunta);
        _alternativaService.criar(salva, dto.alternativas());
        return salva;
    }

    private Pergunta novaPergunta(CriarPerguntaDto dto, Usuario autor, StatusPergunta status, boolean categoriaObrigatoria) {
        validarPergunta(dto.enunciado(), dto.testamento(), dto.nivel());
        _alternativaService.validar(dto.alternativas());
        String enunciado = dto.enunciado().trim();
        if (_perguntaRepository.contarPorEnunciado(enunciado) > 0) {
            throw new IllegalArgumentException("Já existe uma pergunta com este enunciado.");
        }
        String categoria = limpar(dto.categoria(), 80, "A categoria");
        if (categoria == null) {
            if (categoriaObrigatoria) {
                throw new IllegalArgumentException("Informe a categoria.");
            }
            categoria = "História bíblica";
        }
        Pergunta pergunta = dto.toEntity();
        pergunta.setEnunciado(enunciado);
        pergunta.setReferencia(limpar(dto.referencia(), 120, "A referência"));
        pergunta.setCategoria(categoria);
        pergunta.setExplicacao(limpar(dto.explicacao(), 500, "A explicação"));
        pergunta.setStatus(status);
        pergunta.setAtiva(true);
        pergunta.setAutor(autor);
        return pergunta;
    }

    private List<Pergunta> montar(
            Modalidade modalidade,
            Nivel nivel,
            Testamento testamento,
            Collection<Long> excluirIds) {
        return switch (modalidade) {
            case RAPIDO -> juntar(
                    sortear(Nivel.FACIL, 3, testamento, excluirIds),
                    sortear(Nivel.MEDIO, 2, testamento, excluirIds),
                    sortear(Nivel.DIFICIL, 1, testamento, excluirIds)
            );
            case NIVEL -> {
                if (nivel == null) {
                    throw new IllegalArgumentException("Escolha o nível do desafio.");
                }
                yield sortear(nivel, nivel == Nivel.DIFICIL ? 6 : 8, testamento, excluirIds);
            }
            case CLASSICO -> juntar(
                    sortear(Nivel.DIFICIL, QUANTIDADE_DIFICIL, testamento, excluirIds),
                    sortear(Nivel.MEDIO, QUANTIDADE_MEDIO, testamento, excluirIds),
                    sortear(Nivel.FACIL, QUANTIDADE_FACIL, testamento, excluirIds)
            );
        };
    }

    private List<Pergunta> juntar(List<Pergunta> primeira, List<Pergunta> segunda, List<Pergunta> terceira) {
        List<Pergunta> perguntas = new ArrayList<>();
        perguntas.addAll(primeira);
        perguntas.addAll(segunda);
        perguntas.addAll(terceira);
        return perguntas;
    }

    private PerguntaAdminDto paraGestao(Pergunta pergunta) {
        List<AlternativaAdminDto> alternativas = _alternativaService
                .listarParaGestao(List.of(pergunta.getId()))
                .getOrDefault(pergunta.getId(), List.of());
        return PerguntaAdminDto.fromEntity(pergunta, alternativas);
    }

    private List<PerguntaAdminDto> mapearGestao(List<Pergunta> perguntas) {
        List<Long> ids = perguntas.stream().map(Pergunta::getId).toList();
        Map<Long, List<AlternativaAdminDto>> alternativas = _alternativaService.listarParaGestao(ids);
        return perguntas.stream()
                .map(pergunta -> PerguntaAdminDto.fromEntity(
                        pergunta,
                        alternativas.getOrDefault(pergunta.getId(), List.of())
                ))
                .toList();
    }

    private PerguntaDetalheDto detalhe(Pergunta pergunta, long usuarioId, boolean carregarAlternativas) {
        List<AlternativaAdminDto> alternativas = carregarAlternativas
                ? _alternativaService.listarParaGestao(List.of(pergunta.getId())).getOrDefault(pergunta.getId(), List.of())
                : _alternativaService.listarParaGestao(List.of(pergunta.getId())).getOrDefault(pergunta.getId(), List.of());
        boolean propria = pergunta.getAutor() != null && pergunta.getAutor().getId() == usuarioId;
        return new PerguntaDetalheDto(
                pergunta.getId(),
                pergunta.getEnunciado(),
                pergunta.getTestamento(),
                pergunta.getNivel(),
                pergunta.getReferencia(),
                pergunta.getCategoria(),
                pergunta.getExplicacao(),
                pergunta.getStatus() == null ? StatusPergunta.APROVADA : pergunta.getStatus(),
                pergunta.getMotivoRejeicao(),
                nomeDe(pergunta.getAutor()),
                nomeDe(pergunta.getRevisor()),
                pergunta.getCriadaEm(),
                pergunta.getRevisadaEm(),
                propria,
                alternativas
        );
    }

    private PerguntaResumoDto resumo(Pergunta pergunta) {
        return new PerguntaResumoDto(
                pergunta.getId(),
                pergunta.getEnunciado(),
                pergunta.getTestamento(),
                pergunta.getNivel(),
                pergunta.getCategoria(),
                pergunta.getStatus() == null ? StatusPergunta.APROVADA : pergunta.getStatus(),
                nomeDe(pergunta.getAutor()),
                pergunta.getCriadaEm()
        );
    }

    private String nomeDe(Usuario usuario) {
        return usuario == null ? "Banco de perguntas" : usuario.getNome();
    }

    private boolean corresponde(Pergunta pergunta, String termo) {
        String autor = pergunta.getAutor() == null ? "" : pergunta.getAutor().getNome().toLowerCase(Locale.ROOT);
        return pergunta.getEnunciado().toLowerCase(Locale.ROOT).contains(termo)
                || autor.contains(termo)
                || (pergunta.getCategoria() != null && pergunta.getCategoria().toLowerCase(Locale.ROOT).contains(termo));
    }

    private boolean disponivelParaJogo(Pergunta pergunta) {
        return pergunta.isAtiva()
                && (pergunta.getStatus() == null || pergunta.getStatus() == StatusPergunta.APROVADA);
    }

    private List<Pergunta> sortear(
            Nivel nivel,
            int quantidade,
            Testamento testamento,
            Collection<Long> excluirIds) {
        var pageable = PageRequest.of(0, quantidade);
        List<Pergunta> perguntas = testamento == null
                ? _perguntaRepository.sortearPorNivel(nivel, excluirIds, pageable)
                : _perguntaRepository.sortearPorNivelETestamento(nivel, testamento, excluirIds, pageable);
        if (perguntas.size() < quantidade) {
            String complemento = testamento == null ? "" : " no testamento " + testamento;
            throw new IllegalArgumentException(
                    "Não há perguntas suficientes de nível " + nivel + complemento + " que você ainda não respondeu."
            );
        }
        return perguntas;
    }

    private Collection<Long> perguntasJaVistas(long usuarioId) {
        List<Long> ids = _conjuntoRepository.findPerguntaIdsByUsuarioId(usuarioId);
        return ids.isEmpty() ? List.of(-1L) : ids;
    }

    private Usuario exigirAdmin() throws UsuarioNotFoundException {
        if (!_usuarioAutenticadoService.adminNoBanco()) {
            throw new AcessoNegadoException();
        }
        return _usuarioAutenticadoService.atual();
    }

    private void validarPergunta(String enunciado, Testamento testamento, Nivel nivel) {
        if (enunciado == null || enunciado.isBlank()) {
            throw new IllegalArgumentException("O enunciado é obrigatório.");
        }
        if (enunciado.trim().length() > 500) {
            throw new IllegalArgumentException("O enunciado deve ter no máximo 500 caracteres.");
        }
        if (testamento == null) {
            throw new IllegalArgumentException("O testamento é obrigatório.");
        }
        if (nivel == null) {
            throw new IllegalArgumentException("O nível é obrigatório.");
        }
    }

    private String limpar(String valor, int maximo, String rotulo) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        String texto = valor.trim();
        if (texto.length() > maximo) {
            throw new IllegalArgumentException(rotulo + " deve ter no máximo " + maximo + " caracteres.");
        }
        return texto;
    }
}
