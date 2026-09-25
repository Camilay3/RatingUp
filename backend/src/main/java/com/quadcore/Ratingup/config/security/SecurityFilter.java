package com.quadcore.Ratingup.config.security;

import com.quadcore.Ratingup.exception.*;
import com.quadcore.Ratingup.repository.UserRepository;
import com.quadcore.Ratingup.service.TokenCookieService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final  TokenGenerator tokenService;

    private final UserRepository repository;

    private final TokenCookieService tokenCookieService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/conta/cadastro") || path.equals("/auth/login") || path.equals("/auth/reset-password");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var tokenJWT = tokenCookieService.recoverToken(request);

        if (tokenJWT != null) {
            try {
                var subject = tokenService.getLoginSubject(tokenJWT);
                var usuario = repository.findByEmail(subject)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
