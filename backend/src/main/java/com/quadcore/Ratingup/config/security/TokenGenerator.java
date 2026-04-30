package com.quadcore.Ratingup.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.quadcore.Ratingup.model.profile.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenGenerator{
    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("login-basico")
                    .withSubject(user.getEmail())
                    .withExpiresAt(gerarDataExpiracao())
                    .sign(algorithm);
        }
        catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    private Instant gerarDataExpiracao(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String getSubject(String tokenJWT){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("login-basico")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        }
        catch (JWTVerificationException exception){
            throw new RuntimeException("Token JWT inválido ou expirado",exception);
        }
    }
}
