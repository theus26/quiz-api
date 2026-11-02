package com.mathe.quiz_biblico_api.dto.auth;

import com.mathe.quiz_biblico_api.Enum.Role;

import java.time.Instant;

public record TokenDto(String token, String tipo, long usuarioId, Role role, Instant expiraEm) {
}
