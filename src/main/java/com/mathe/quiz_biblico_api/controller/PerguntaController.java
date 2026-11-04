package com.mathe.quiz_biblico_api.controller;

import com.mathe.quiz_biblico_api.Enum.FiltroTestamento;
import com.mathe.quiz_biblico_api.Exception.PerguntaNotFoundException;
import com.mathe.quiz_biblico_api.Exception.UsuarioNotFoundException;
import com.mathe.quiz_biblico_api.dto.PaginacaoDto;
import com.mathe.quiz_biblico_api.dto.alternativa.ConferirAlternativaDto;
import com.mathe.quiz_biblico_api.dto.pergunta.AtualizarPerguntaDto;
import com.mathe.quiz_biblico_api.dto.pergunta.CriarPerguntaDto;
import com.mathe.quiz_biblico_api.dto.pergunta.PerguntaAdminDto;
import com.mathe.quiz_biblico_api.dto.pergunta.PerguntaComAlternativasDto;
import com.mathe.quiz_biblico_api.service.PerguntaService;
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

    public PerguntaController(PerguntaService perguntaService) {
        this.perguntaService = perguntaService;
    }

    @PostMapping
    public ResponseEntity<PerguntaAdminDto> criar(@RequestBody CriarPerguntaDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(perguntaService.criar(dto));
    }

    @GetMapping
    public List<PerguntaComAlternativasDto> listar(
            @RequestParam(required = false) FiltroTestamento testamento) throws UsuarioNotFoundException {
        return perguntaService.listar(testamento);
    }

    @GetMapping("/gerenciar")
    public PaginacaoDto<PerguntaAdminDto> gerenciar(
            @RequestParam(defaultValue = "1") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {
        return perguntaService.gerenciar(pagina, tamanho);
    }

    @GetMapping("/{id}")
    public PerguntaComAlternativasDto obter(@PathVariable Long id) throws PerguntaNotFoundException {
        return perguntaService.obter(id);
    }

    @PutMapping("/{id}")
    public PerguntaAdminDto atualizar(
            @PathVariable Long id,
            @RequestBody AtualizarPerguntaDto dto) throws PerguntaNotFoundException {
        return perguntaService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) throws PerguntaNotFoundException {
        perguntaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/conferir")
    public boolean conferir(@RequestBody ConferirAlternativaDto dto) {
        return perguntaService.conferir(dto.perguntaId(), dto.alternativaId());
    }
}
