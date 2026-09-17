package com.quadcore.Ratingup.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenCookieService {

    @Value("${api.security.cookie-secure}")
    private boolean cookieSecure;

    public String recoverToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie: cookies){
            if("token".equals(cookie.getName())) return cookie.getValue();
        }

        return null;
    }

    public boolean isCookieSecure() {
        return cookieSecure;
    }
}
