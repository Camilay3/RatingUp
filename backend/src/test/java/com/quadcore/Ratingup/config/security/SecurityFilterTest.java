package com.quadcore.Ratingup.config.security;
import com.quadcore.Ratingup.enums.Roles;

import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.repository.UserRepository;
import com.quadcore.Ratingup.service.TokenCookieService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecurityFilterTest {

    @Mock
    private TokenGenerator tokenService;

    @Mock
    private UserRepository repository;

    @Mock
    private FilterChain filterChain;

    @Mock
    private TokenCookieService tokenCookieService;

    @InjectMocks
    private SecurityFilter securityFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should skip filter for login")
    void testShouldNotFilter() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/auth/login");

        assertTrue(securityFilter.shouldNotFilter(request));
    }

    @Test
    @DisplayName("Should authenticate when token is valid")
    void testDoFilterInternal_ValidToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        User user = new User();
        user.setEmail("test@test.com");
        user.setRole(Roles.USER);

        when(tokenCookieService.recoverToken(request))
                .thenReturn("valid-token");

        when(tokenService.getLoginSubject("valid-token"))
                .thenReturn("test@test.com");

        when(repository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        securityFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Should clear context when token is invalid")
    void testDoFilterInternal_InvalidToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String token = "invalid-token";

        when(tokenCookieService.recoverToken(request))
                .thenReturn(token);

        when(tokenService.getLoginSubject(token))
                .thenThrow(new RuntimeException("Invalid token"));

        SecurityContextHolder.clearContext();

        securityFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Should do nothing when token is absent")
    void testDoFilterInternal_NoToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        securityFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
