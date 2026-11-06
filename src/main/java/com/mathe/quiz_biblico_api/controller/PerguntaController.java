package com.mathe.quiz_biblico_api.controller;

import com.mathe.quiz_biblico_api.Enum.FiltroTestamento;
import com.mathe.quiz_biblico_api.Enum.StatusPergunta;
import com.mathe.quiz_biblico_api.Exception.PerguntaNotFoundException;
import com.mathe.quiz_biblico_api.Exception.UsuarioNotFoundException;
import com.mathe.quiz_biblico_api.dto.PaginacaoDto;
import com.mathe.quiz_biblico_api.dto.alternativa.AlternativaDto;
import com.mathe.quiz_biblico_api.dto.alternativa.ConferirAlternativaDto;
import com.mathe.quiz_biblico_api.dto.pergunta.AtualizarPerguntaDto;
import com.mathe.quiz_biblico_api.dto.pergunta.CriarPerguntaDto;
import com.mathe.quiz_biblico_api.dto.pergunta.PainelModeracaoDto;
import com.mathe.quiz_biblico_api.dto.pergunta.PerguntaAdminDto;
import com.mathe.quiz_biblico_api.dto.pergunta.PerguntaComAlternativasDto;
import com.mathe.quiz_biblico_api.dto.pergunta.PerguntaDetalheDto;
import com.mathe.quiz_biblico_api.dto.pergunta.PerguntaResumoDto;
import com.mathe.quiz_biblico_api.dto.pergunta.RejeitarPerguntaDto;
import com.mathe.quiz_biblico_api.service.PerguntaService;
import com.mathe.quiz_biblico_api.service.RodadaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/perguntas")
public class PerguntaController {
    private final PerguntaService perguntaService;
    private final RodadaService rodadaService;

    public PerguntaController(PerguntaService perguntaService, RodadaService rodadaService) {
        this.perguntaService = perguntaService;
        this.rodadaService = rodadaService;
    }

    @PostMapping("/enviar")
    public ResponseEntity<PerguntaDetalheDto> enviar(@RequestBody CriarPerguntaDto dto) throws UsuarioNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(perguntaService.enviar(dto));
    }

    @GetMapping("/minhas")
    public List<PerguntaResumoDto> minhas() throws UsuarioNotFoundException {
        return perguntaService.minhas();
    }

    @GetMapping("/minhas/{id}")
    public PerguntaDetalheDto minha(@PathVariable Long id) throws PerguntaNotFoundException, UsuarioNotFoundException {
        return perguntaService.minha(id);
    }

    @GetMapping("/moderacao")
    public PainelModeracaoDto moderar(
            @RequestParam(required = false) StatusPergunta status,
            @RequestParam(required = false) String busca) throws UsuarioNotFoundException {
        return perguntaService.moderar(status, busca);
    }

    @GetMapping("/moderacao/{id}")
    public PerguntaDetalheDto detalheModeracao(@PathVariable Long id) throws PerguntaNotFoundException, UsuarioNotFoundException {
        return perguntaService.detalheModeracao(id);
    }

    @PostMapping("/{id}/aprovar")
    public PerguntaDetalheDto aprovar(@PathVariable Long id) throws PerguntaNotFoundException, UsuarioNotFoundException {
        return perguntaService.aprovar(id);
    }

    @PostMapping("/{id}/rejeitar")
    public PerguntaDetalheDto rejeitar(@PathVariable Long id, @RequestBody RejeitarPerguntaDto dto)
            throws PerguntaNotFoundException, UsuarioNotFoundException {
        return perguntaService.rejeitar(id, dto == null ? null : dto.motivo());
    }

    @PostMapping
    public ResponseEntity<PerguntaAdminDto> criar(@RequestBody CriarPerguntaDto dto) throws UsuarioNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(perguntaService.criar(dto));
    }

    @GetMapping
    public List<PerguntaComAlternativasDto> listar(
            @RequestParam(required = false) FiltroTestamento testamento) {
        return perguntaService.listar(testamento);
    }

    @GetMapping("/gerenciar")
    public PaginacaoDto<PerguntaAdminDto> gerenciar(
            @RequestParam(defaultValue = "1") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) throws UsuarioNotFoundException {
        return perguntaService.gerenciar(pagina, tamanho);
    }

    @GetMapping("/{id}")
    public PerguntaComAlternativasDto obter(@PathVariable Long id) throws PerguntaNotFoundException {
        return perguntaService.obter(id);
    }

    @PutMapping("/{id}")
    public PerguntaAdminDto atualizar(
            @PathVariable Long id,
            @RequestBody AtualizarPerguntaDto dto) throws PerguntaNotFoundException, UsuarioNotFoundException {
        return perguntaService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) throws PerguntaNotFoundException, UsuarioNotFoundException {
        perguntaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/conferir")
    public boolean conferir(@RequestBody ConferirAlternativaDto dto) throws PerguntaNotFoundException {
        if (dto.rodadaToken() == null || dto.rodadaToken().isBlank()) {
            throw new IllegalArgumentException("A partida não foi informada.");
        }
        return rodadaService.conferir(dto.rodadaToken(), dto.perguntaId(), dto.alternativaId());
    }

    @GetMapping("/{id}/correta")
    public AlternativaDto revelarCorreta(
            @PathVariable Long id,
            @RequestParam(required = false) String rodada) throws PerguntaNotFoundException {
        if (rodada == null || rodada.isBlank()) {
            throw new IllegalArgumentException("A partida não foi informada.");
        }
        return rodadaService.revelar(rodada, id);
    }
}
