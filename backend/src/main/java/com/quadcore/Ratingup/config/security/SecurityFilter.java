package com.quadcore.Ratingup.config.security;

import com.quadcore.Ratingup.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private  TokenGenerator tokenService;

    @Autowired
    private UserRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            var tokenJWT = recuperarToken(request);

            if (tokenJWT != null){
                var subject = tokenService.getSubject(tokenJWT); //nessa parte aq precisa da função de autenticação que ta sendo feita pelo kalebe
                var usuario = repository.findByEmail(subject);

                var authentication = new UsernamePasswordAuthenticationToken(usuario,null,usuario.getAuthorities()); //roles sendo configuradas pelo davi

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request,response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null){
            return authorizationHeader.replace("Bearer ","").trim();
        }
        return null;
    }
}
