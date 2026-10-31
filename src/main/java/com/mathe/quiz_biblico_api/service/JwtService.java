package com.mathe.quiz_biblico_api.service;

import com.mathe.quiz_biblico_api.dto.auth.TokenDto;
import com.mathe.quiz_biblico_api.model.Role;
import com.mathe.quiz_biblico_api.model.Usuario;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {
    private final byte[] secret;
    private final Duration expiracao;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiracao-minutos}") long expiracaoMinutos) {
        byte[] secretBytes = secret == null ? new byte[0] : secret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalStateException("app.jwt.secret precisa ter pelo menos 32 bytes.");
        }
        if (expiracaoMinutos < 1) {
            throw new IllegalStateException("app.jwt.expiracao-minutos precisa ser maior que zero.");
        }
        this.secret = secretBytes;
        this.expiracao = Duration.ofMinutes(expiracaoMinutos);
    }

    public TokenDto gerarToken(Usuario usuario) {
        Instant agora = Instant.now();
        Instant expiraEm = agora.plus(expiracao);
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(usuario.getEmail())
                .claim("role", usuario.getRole().name())
                .claim("usuarioId", usuario.getId())
                .issueTime(Date.from(agora))
                .expirationTime(Date.from(expiraEm))
                .build();

        SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
        try {
            jwt.sign(new MACSigner(secret));
        } catch (JOSEException ex) {
            throw new IllegalStateException("Não foi possível gerar o token.", ex);
        }
        return new TokenDto(jwt.serialize(), "Bearer", usuario.getId(), usuario.getRole(), expiraEm);
    }

    public Optional<TokenUsuario> lerToken(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            if (!jwt.verify(new MACVerifier(secret))) {
                return Optional.empty();
            }

            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            Date expiracaoToken = claims.getExpirationTime();
            if (expiracaoToken == null || !expiracaoToken.after(new Date())) {
                return Optional.empty();
            }

            String role = claims.getStringClaim("role");
            if (claims.getSubject() == null || role == null) {
                return Optional.empty();
            }
            return Optional.of(new TokenUsuario(claims.getSubject(), Role.valueOf(role)));
        } catch (ParseException | JOSEException | IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    public record TokenUsuario(String email, Role role) {
    }
}
