package com.mathe.quiz_biblico_api.controller;

import com.mathe.quiz_biblico_api.dto.auth.LoginDto;
import com.mathe.quiz_biblico_api.dto.auth.TokenDto;
import com.mathe.quiz_biblico_api.service.AutenticacaoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
    private final AutenticacaoService autenticacaoService;

    public AutenticacaoController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping("/login")
    public TokenDto login(@RequestBody LoginDto dto) {
        return autenticacaoService.login(dto);
    }
}
