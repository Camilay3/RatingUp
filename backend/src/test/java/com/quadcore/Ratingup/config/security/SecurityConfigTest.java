package com.quadcore.Ratingup.config.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SecurityConfigTest {

    @Mock
    private SecurityFilter securityFilter;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    @DisplayName("Should initialize PasswordEncoder")
    void testPasswordEncoder() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
    }

    @Test
    @DisplayName("Should initialize AuthenticationManager")
    void testAuthenticationManager() throws Exception {
        AuthenticationConfiguration configuration = mock(AuthenticationConfiguration.class);
        AuthenticationManager managerMock = mock(AuthenticationManager.class);
        when(configuration.getAuthenticationManager()).thenReturn(managerMock);

        AuthenticationManager manager = securityConfig.authenticationManager(configuration);
        assertNotNull(manager);
    }

    @Test
    @DisplayName("Should initialize CorsConfigurationSource")
    void testCorsConfigurationSource() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        assertNotNull(source);
    }
}
