package com.quadcore.Ratingup.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "emailOrigem", "test@example.com");
        ReflectionTestUtils.setField(emailService, "nomeEnviador", "Test Sender");
    }

    @Test
    @DisplayName("Should send recover mail successfully")
    void testSendRecoverMail() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        
        when(templateEngine.process(eq("email/redefinir-senha"), any(Context.class))).thenReturn("<html>test</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendRecoverMail("destiny@example.com", "token123");

        verify(mailSender, times(1)).send(mimeMessage);
    }
    
    @Test
    @DisplayName("Should use fallback mail sender when primary fails")
    void testFallbackMailSender() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        
        when(templateEngine.process(eq("email/redefinir-senha"), any(Context.class))).thenReturn("<html>test</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new RuntimeException("Simulated failure")).when(mailSender).send(mimeMessage);
        
        JavaMailSender fallbackMock = mock(JavaMailSender.class);
        when(fallbackMock.createMimeMessage()).thenReturn(mimeMessage);
        ReflectionTestUtils.setField(emailService, "fallbackMailSender", fallbackMock);
        
        emailService.sendRecoverMail("destiny@example.com", "token123");
        
        verify(mailSender, times(1)).send(mimeMessage);
        verify(fallbackMock, times(1)).send(mimeMessage);
    }
}
