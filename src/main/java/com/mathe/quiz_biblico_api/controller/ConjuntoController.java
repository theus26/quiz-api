package com.mathe.quiz_biblico_api.controller;

import com.mathe.quiz_biblico_api.Enum.FiltroTestamento;
import com.mathe.quiz_biblico_api.Exception.UsuarioNotFoundException;
import com.mathe.quiz_biblico_api.service.ConjuntoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conjuntos")
public class ConjuntoController {
    private final ConjuntoService conjuntoService;

    public ConjuntoController(ConjuntoService conjuntoService) {
        this.conjuntoService = conjuntoService;
    }

    @DeleteMapping
    public ResponseEntity<Void> limpar(@RequestParam(required = false) FiltroTestamento testamento)
            throws UsuarioNotFoundException {
        conjuntoService.limpar(testamento);
        return ResponseEntity.noContent().build();
    }
}
