package com.mathe.quiz_biblico_api.service;

import com.mathe.quiz_biblico_api.Enum.Modalidade;
import com.mathe.quiz_biblico_api.Enum.ResultadoPartida;
import com.mathe.quiz_biblico_api.Enum.StatusPergunta;
import com.mathe.quiz_biblico_api.Exception.AcessoNegadoException;
import com.mathe.quiz_biblico_api.Exception.PerguntaNotFoundException;
import com.mathe.quiz_biblico_api.dto.alternativa.AlternativaDto;
import com.mathe.quiz_biblico_api.dto.partida.AbrirPartidaDto;
import com.mathe.quiz_biblico_api.dto.partida.PartidaAbertaDto;
import com.mathe.quiz_biblico_api.dto.pergunta.PerguntaComAlternativasDto;
import com.mathe.quiz_biblico_api.model.Pergunta;
import com.mathe.quiz_biblico_api.model.Rodada;
import com.mathe.quiz_biblico_api.model.RodadaPergunta;
import com.mathe.quiz_biblico_api.model.RodadaTentativa;
import com.mathe.quiz_biblico_api.model.Usuario;
import com.mathe.quiz_biblico_api.repository.AlternativaRepository;
import com.mathe.quiz_biblico_api.repository.PerguntaRepository;
import com.mathe.quiz_biblico_api.repository.RodadaPerguntaRepository;
import com.mathe.quiz_biblico_api.repository.RodadaRepository;
import com.mathe.quiz_biblico_api.repository.RodadaTentativaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RodadaService {
    private static final Duration VALIDADE = Duration.ofHours(12);

    private final RodadaRepository rodadaRepository;
    private final RodadaPerguntaRepository rodadaPerguntaRepository;
    private final RodadaTentativaRepository tentativaRepository;
    private final PerguntaRepository perguntaRepository;
    private final AlternativaRepository alternativaRepository;
    private final AlternativaService alternativaService;
    private final PerguntaService perguntaService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public RodadaService(
            RodadaRepository rodadaRepository,
            RodadaPerguntaRepository rodadaPerguntaRepository,
            RodadaTentativaRepository tentativaRepository,
            PerguntaRepository perguntaRepository,
            AlternativaRepository alternativaRepository,
            AlternativaService alternativaService,
            PerguntaService perguntaService,
            UsuarioAutenticadoService usuarioAutenticadoService) {
        this.rodadaRepository = rodadaRepository;
        this.rodadaPerguntaRepository = rodadaPerguntaRepository;
        this.tentativaRepository = tentativaRepository;
        this.perguntaRepository = perguntaRepository;
        this.alternativaRepository = alternativaRepository;
        this.alternativaService = alternativaService;
        this.perguntaService = perguntaService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @Transactional(rollbackFor = Exception.class)
    public PartidaAbertaDto abrir(AbrirPartidaDto dto) {
        if (dto == null || dto.filtroTestamento() == null) {
            throw new IllegalArgumentException("Escolha o testamento.");
        }
        if (dto.modalidade() == null) {
            throw new IllegalArgumentException("Escolha a modalidade.");
        }
        if (dto.modalidade() == Modalidade.NIVEL && dto.nivel() == null) {
            throw new IllegalArgumentException("Escolha o nível do desafio.");
        }
        if (dto.modalidade() != Modalidade.NIVEL && dto.nivel() != null) {
            throw new IllegalArgumentException("O nível só é usado no desafio por dificuldade.");
        }

        List<PerguntaComAlternativasDto> perguntas = perguntaService.sortearParaPartida(
                dto.filtroTestamento(),
                dto.modalidade(),
                dto.nivel()
        );
        Rodada rodada = new Rodada();
        rodada.setToken(UUID.randomUUID().toString());
        rodada.setFiltroTestamento(dto.filtroTestamento());
        rodada.setModalidade(dto.modalidade());
        rodada.setNivel(dto.modalidade() == Modalidade.NIVEL ? dto.nivel() : null);
        usuarioAutenticadoService.atualOpcional()
                .filter(Usuario::getAtivo)
                .ifPresent(rodada::setUsuario);
        Rodada salva = rodadaRepository.save(rodada);

        List<RodadaPergunta> vinculos = new ArrayList<>();
        for (PerguntaComAlternativasDto pergunta : perguntas) {
            RodadaPergunta vinculo = new RodadaPergunta();
            vinculo.setRodada(salva);
            vinculo.setPerguntaId(pergunta.id());
            vinculos.add(vinculo);
        }
        rodadaPerguntaRepository.saveAll(vinculos);
        return new PartidaAbertaDto(
                salva.getToken(),
                salva.getFiltroTestamento(),
                salva.getModalidade(),
                salva.getNivel(),
                perguntas
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean conferir(String token, long perguntaId, long alternativaId) throws PerguntaNotFoundException {
        Rodada rodada = bloquear(token);
        if (!rodadaPerguntaRepository.existsByRodadaIdAndPerguntaId(rodada.getId(), perguntaId)) {
            throw new IllegalArgumentException("Esta pergunta não faz parte da partida.");
        }
        Optional<RodadaTentativa> existente = tentativaRepository.findByRodadaIdAndPerguntaId(rodada.getId(), perguntaId);
        if (existente.isPresent()) {
            return existente.get().isCorreta();
        }
        Optional<Boolean> resultado = alternativaRepository.buscarCorreta(perguntaId, alternativaId);
        if (resultado.isEmpty()) {
            throw new IllegalArgumentException("Alternativa inválida para esta pergunta.");
        }
        Pergunta pergunta = perguntaRepository.findById(perguntaId).orElseThrow(PerguntaNotFoundException::new);
        if (!pergunta.isAtiva() || (pergunta.getStatus() != null && pergunta.getStatus() != StatusPergunta.APROVADA)) {
            throw new IllegalArgumentException("Esta pergunta não está disponível.");
        }
        RodadaTentativa tentativa = new RodadaTentativa();
        tentativa.setRodada(rodada);
        tentativa.setPerguntaId(perguntaId);
        tentativa.setAlternativaId(alternativaId);
        tentativa.setCorreta(resultado.get());
        tentativaRepository.saveAndFlush(tentativa);
        return tentativa.isCorreta();
    }

    @Transactional(rollbackFor = Exception.class)
    public AlternativaDto revelar(String token, long perguntaId) throws PerguntaNotFoundException {
        Rodada rodada = bloquear(token);
        if (!rodadaPerguntaRepository.existsByRodadaIdAndPerguntaId(rodada.getId(), perguntaId)) {
            throw new IllegalArgumentException("Esta pergunta não faz parte da partida.");
        }
        int limite = rodada.getModalidade() == Modalidade.RAPIDO ? 0 : 1;
        if (rodada.getRevelacoes() >= limite) {
            throw new IllegalArgumentException(limite == 0
                    ? "O desafio rápido não revela a resposta."
                    : "A revelação desta partida já foi usada.");
        }
        rodada.setRevelacoes(rodada.getRevelacoes() + 1);
        return alternativaService.revelarCorreta(perguntaId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Encerramento encerrar(String token, ResultadoPartida informado, Usuario usuario) throws PerguntaNotFoundException {
        Rodada rodada = bloquear(token);
        if (rodada.getUsuario() != null && rodada.getUsuario().getId() != usuario.getId()) {
            throw new AcessoNegadoException();
        }
        List<RodadaTentativa> tentativas = tentativaRepository.findByRodadaId(rodada.getId());
        if (tentativas.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma resposta foi registrada nesta partida.");
        }
        List<Long> ids = tentativas.stream().map(RodadaTentativa::getPerguntaId).toList();
        Map<Long, Pergunta> perguntas = perguntaRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Pergunta::getId, Function.identity()));
        List<CalculadoraPontuacao.TentativaAvaliada> avaliadas = new ArrayList<>();
        List<Pergunta> respondidas = new ArrayList<>();
        for (RodadaTentativa tentativa : tentativas) {
            Pergunta pergunta = perguntas.get(tentativa.getPerguntaId());
            if (pergunta == null) {
                throw new PerguntaNotFoundException();
            }
            avaliadas.add(new CalculadoraPontuacao.TentativaAvaliada(tentativa.isCorreta(), pergunta.getNivel()));
            respondidas.add(pergunta);
        }
        CalculadoraPontuacao.Resultado calculo = CalculadoraPontuacao.calcular(avaliadas, informado);
        rodada.setConsumida(true);
        rodada.setUsuario(usuario);
        return new Encerramento(rodada, calculo, respondidas);
    }

    private Rodada bloquear(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("A partida não foi informada.");
        }
        Rodada rodada = rodadaRepository.findByTokenParaAtualizar(token.trim())
                .orElseThrow(() -> new IllegalArgumentException("Partida não encontrada."));
        if (rodada.isConsumida()) {
            throw new IllegalArgumentException("Esta partida já foi registrada.");
        }
        if (rodada.getCriadaEm() != null && rodada.getCriadaEm().isBefore(Instant.now().minus(VALIDADE))) {
            throw new IllegalArgumentException("Esta partida expirou. Comece outra.");
        }
        return rodada;
    }

    public record Encerramento(Rodada rodada, CalculadoraPontuacao.Resultado calculo, List<Pergunta> perguntas) {
    }
}
