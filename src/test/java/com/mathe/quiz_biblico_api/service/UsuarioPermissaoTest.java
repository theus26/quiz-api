package com.mathe.quiz_biblico_api.service;

import com.mathe.quiz_biblico_api.Enum.Role;
import com.mathe.quiz_biblico_api.Exception.AcessoNegadoException;
import com.mathe.quiz_biblico_api.dto.usuario.AlterarPapelDto;
import com.mathe.quiz_biblico_api.dto.usuario.AtualizarUsuarioDto;
import com.mathe.quiz_biblico_api.model.Usuario;
import com.mathe.quiz_biblico_api.repository.ConjuntoRepository;
import com.mathe.quiz_biblico_api.repository.PerguntaRepository;
import com.mathe.quiz_biblico_api.repository.PontuacaoRepository;
import com.mathe.quiz_biblico_api.repository.RodadaRepository;
import com.mathe.quiz_biblico_api.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UsuarioPermissaoTest {

    @Test
    void atualizarUsuarioNaoTemCampoDePerfil() {
        boolean temPapel = Arrays.stream(AtualizarUsuarioDto.class.getRecordComponents())
                .anyMatch(componente -> componente.getName().equals("role"));
        assertFalse(temPapel);
    }

    @Test
    void usuarioComumNaoAlteraPapel() {
        UsuarioAutenticadoService autenticado = mock(UsuarioAutenticadoService.class);
        when(autenticado.adminNoBanco()).thenReturn(false);
        usuarioService servico = servico(autenticado, mock(UsuarioRepository.class));

        assertThrows(AcessoNegadoException.class, () -> servico.alterarPapel(2L, new AlterarPapelDto(Role.ADMIN)));
    }

    @Test
    void administradorNaoMudaOProprioPapel() throws Exception {
        UsuarioRepository usuarios = mock(UsuarioRepository.class);
        UsuarioAutenticadoService autenticado = mock(UsuarioAutenticadoService.class);
        Usuario admin = new Usuario("Admin", "admin@quiz.local", "hash");
        admin.setId(1);
        admin.setRole(Role.ADMIN);
        when(autenticado.adminNoBanco()).thenReturn(true);
        when(autenticado.atual()).thenReturn(admin);
        usuarioService servico = servico(autenticado, usuarios);

        assertThrows(IllegalArgumentException.class, () -> servico.alterarPapel(1L, new AlterarPapelDto(Role.USER)));
        verify(usuarios, never()).save(org.mockito.ArgumentMatchers.any());
    }

    private static usuarioService servico(UsuarioAutenticadoService autenticado, UsuarioRepository usuarios) {
        return new usuarioService(
                usuarios,
                mock(ConjuntoRepository.class),
                mock(PontuacaoRepository.class),
                mock(PerguntaRepository.class),
                mock(RodadaRepository.class),
                autenticado,
                mock(PasswordEncoder.class)
        );
    }
}
