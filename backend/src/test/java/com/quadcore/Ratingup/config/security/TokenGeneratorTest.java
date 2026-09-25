package com.quadcore.Ratingup.config.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.quadcore.Ratingup.model.profile.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TokenGeneratorTest {

    @InjectMocks
    private TokenGenerator tokenGenerator;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenGenerator, "secret", "my-secret");
    }

    @Test
    @DisplayName("Should generate token successfully")
    void testGerarToken() {
        User user = new User();
        user.setEmail("test@test.com");

        String token = tokenGenerator.gerarToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Should get subject successfully")
    void testGetSubject() {
        User user = new User();
        user.setEmail("test@test.com");

        String token = tokenGenerator.gerarToken(user);
        String subject = tokenGenerator.getSubject(token);

        assertEquals("test@test.com", subject);
    }

    @Test
    @DisplayName("Should throw when get subject with invalid token")
    void testGetSubjectInvalid() {
        assertThrows(Exception.class, () -> tokenGenerator.getSubject("invalid-token"));
    }
}
