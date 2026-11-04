package com.mathe.quiz_biblico_api.controller;

import com.mathe.quiz_biblico_api.Exception.PerguntaNotFoundException;
import com.mathe.quiz_biblico_api.Exception.UsuarioNotFoundException;
import com.mathe.quiz_biblico_api.dto.pontuacao.CriarPontuacaoDto;
import com.mathe.quiz_biblico_api.dto.pontuacao.PontuacaoDto;
import com.mathe.quiz_biblico_api.dto.pontuacao.RankingDto;
import com.mathe.quiz_biblico_api.service.PontuacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pontuacoes")
public class PontuacaoController {
    private final PontuacaoService pontuacaoService;

    public PontuacaoController(PontuacaoService pontuacaoService) {
        this.pontuacaoService = pontuacaoService;
    }

    @PostMapping
    public ResponseEntity<PontuacaoDto> registrar(@RequestBody CriarPontuacaoDto dto)
            throws UsuarioNotFoundException, PerguntaNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(pontuacaoService.registrar(dto));
    }

    @GetMapping("/ranking")
    public List<RankingDto> ranking() {
        return pontuacaoService.ranking();
    }

    @GetMapping("/minhas")
    public List<PontuacaoDto> minhas() throws UsuarioNotFoundException {
        return pontuacaoService.minhas();
    }
}
