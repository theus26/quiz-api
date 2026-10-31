package com.mathe.quiz_biblico_api.controller;

import com.mathe.quiz_biblico_api.Exception.UsuarioNotFoundException;
import com.mathe.quiz_biblico_api.dto.PaginacaoDto;
import com.mathe.quiz_biblico_api.dto.usuario.AtualizarUsuarioDto;
import com.mathe.quiz_biblico_api.dto.usuario.CriarUsuarioDto;
import com.mathe.quiz_biblico_api.dto.usuario.UsuarioDto;
import com.mathe.quiz_biblico_api.service.usuarioService;
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

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final usuarioService usuarioService;

    public UsuarioController(usuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioDto> criar(@RequestBody CriarUsuarioDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.CriarUsuario(dto));
    }

    @GetMapping
    public PaginacaoDto<UsuarioDto> listar(
            @RequestParam(defaultValue = "1") int pagina,
            @RequestParam(defaultValue = "10") int tamanho,
            @RequestParam(required = false) Boolean ativo) {
        return usuarioService.listarUsuarios(pagina, tamanho, ativo);
    }

    @GetMapping("/{id}")
    public UsuarioDto obter(@PathVariable Long id) throws UsuarioNotFoundException {
        return usuarioService.obterUsuarioPorId(id);
    }

    @PutMapping("/{id}")
    public UsuarioDto atualizar(@PathVariable Long id, @RequestBody AtualizarUsuarioDto dto) throws UsuarioNotFoundException {
        return usuarioService.AtualizarUsuario(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) throws UsuarioNotFoundException {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
