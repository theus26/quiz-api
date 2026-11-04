package com.mathe.quiz_biblico_api.service;

import com.mathe.quiz_biblico_api.Enum.FiltroTestamento;
import com.mathe.quiz_biblico_api.Enum.Testamento;
import com.mathe.quiz_biblico_api.Exception.UsuarioNotFoundException;
import com.mathe.quiz_biblico_api.repository.ConjuntoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConjuntoService {
    private final ConjuntoRepository _conjuntoRepository;
    private final UsuarioAutenticadoService _usuarioAutenticadoService;

    public ConjuntoService(
            ConjuntoRepository conjuntoRepository,
            UsuarioAutenticadoService usuarioAutenticadoService) {
        this._conjuntoRepository = conjuntoRepository;
        this._usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @Transactional(rollbackFor = Exception.class)
    public void limpar(FiltroTestamento filtro) throws UsuarioNotFoundException {
        long usuarioId = _usuarioAutenticadoService.atual().getId();
        if (filtro == null || filtro == FiltroTestamento.AMBOS) {
            _conjuntoRepository.deleteByUsuarioId(usuarioId);
            return;
        }
        _conjuntoRepository.deleteByUsuarioIdAndPerguntaTestamento(usuarioId, Testamento.valueOf(filtro.name()));
    }
}
