package com.quadcore.Ratingup.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.quadcore.Ratingup.model.profile.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenGenerator{
    @Value("${api.security.token.secret}")
    private String secret;

    //essa função está sendo usada tanto para o token de login como de recuperação, o que dá o mesmo tratamento a tokens com funções diferentes, é melhor separar essa geração em duas funções distintas e diferenciar os detalhes de cada uma
    public String generateLoginToken(User user){
        return generateToken(user,"login-basico", Duration.ofHours(2));
    }

    public String generateRecoveryToken(User user){
        return generateToken(user,"recuperar-conta", Duration.ofMinutes(10));
    }

    private String generateToken(User user, String issuer, Duration expiration){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.getEmail())
                    .withExpiresAt(Instant.now().plus(expiration))
                    .sign(algorithm);
        }
        catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    public String getLoginSubject(String tokenJWT){
        return getSubject(tokenJWT, "login-basico");
    }

    public String getRecoverySubject(String tokenJWT){
        return getSubject(tokenJWT, "recuperar-conta");
    }

    private String getSubject(String tokenJWT, String issuer){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        }
        catch (JWTVerificationException exception){
            throw new RuntimeException("Token JWT inválido ou expirado",exception);
        }
    }
}
