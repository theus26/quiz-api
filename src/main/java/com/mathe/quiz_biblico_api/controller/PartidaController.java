package com.mathe.quiz_biblico_api.controller;

import com.mathe.quiz_biblico_api.dto.partida.AbrirPartidaDto;
import com.mathe.quiz_biblico_api.dto.partida.PartidaAbertaDto;
import com.mathe.quiz_biblico_api.service.RodadaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/partidas")
public class PartidaController {
    private final RodadaService rodadaService;

    public PartidaController(RodadaService rodadaService) {
        this.rodadaService = rodadaService;
    }

    @PostMapping
    public PartidaAbertaDto abrir(@RequestBody AbrirPartidaDto dto) {
        return rodadaService.abrir(dto);
    }
}
