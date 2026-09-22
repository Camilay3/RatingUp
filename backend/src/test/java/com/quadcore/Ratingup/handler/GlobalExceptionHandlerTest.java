package com.quadcore.Ratingup.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.quadcore.Ratingup.dto.response.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Should handle EntityNotFoundException")
    void testEntityNotFound() {
        ResponseEntity<ApiResponse<?>> response = handler.handleNotFound(new EntityNotFoundException("Not found"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not found", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle DataIntegrityViolationException")
    void testDataIntegrity() {
        ResponseEntity<ApiResponse<?>> response = handler.handleDataIntegrity(new DataIntegrityViolationException("Conflict"));
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Violação de integridade de dados", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle JWT Exceptions")
    void testJwt() {
        ResponseEntity<ApiResponse<?>> response = handler.handleJwt(new JWTVerificationException("Invalid token"));
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token inválido ou expirado", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle AuthenticationException")
    void testAuthentication() {
        AuthenticationException ex = new AuthenticationException("Auth failed") {};
        ResponseEntity<ApiResponse<?>> response = handler.handleAuthentication(ex);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Não autenticado", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle AccessDeniedException")
    void testAccessDenied() {
        ResponseEntity<ApiResponse<?>> response = handler.handleAccessDenied(new AccessDeniedException("Denied"));
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Acesso negado", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle MailException")
    void testMail() {
        MailException ex = new MailException("Mail failed") {};
        ResponseEntity<ApiResponse<?>> response = handler.handleMail(ex);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Falha ao enviar e-mail. Tente novamente mais tarde.", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle RuntimeException")
    void testRuntime() {
        ResponseEntity<ApiResponse<?>> response = handler.handleRuntime(new RuntimeException("Runtime error"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Runtime error", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle general Exception")
    void testGeneral() {
        ResponseEntity<ApiResponse<?>> response = handler.handleGeneral(new Exception("General error"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro interno no servidor", response.getBody().message());
    }
    
    @Test
    @DisplayName("Should handle ValidationException")
    void testValidation() {
        ValidationException ex = new ValidationException("field1", "Invalid data");
        ResponseEntity<ApiResponse<?>> response = handler.handleValidation(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Dados inválidos", response.getBody().message());
        Map<String, List<String>> errors = (Map<String, List<String>>) response.getBody().data();
        assertEquals("Invalid data", errors.get("field1").get(0));
    }
    
    @Test
    @DisplayName("Should handle DuplicateFieldException")
    void testDuplicateField() {
        DuplicateFieldException ex = new DuplicateFieldException(List.of("Error 1"));
        ResponseEntity<ErrorResponse> response = handler.handleDuplicate(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("CONFLICT", response.getBody().code());
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException")
    void testMethodArgumentNotValid() {
        org.springframework.validation.BindingResult bindingResult = org.mockito.Mockito.mock(org.springframework.validation.BindingResult.class);
        org.mockito.Mockito.when(bindingResult.getFieldErrors()).thenReturn(List.of(new org.springframework.validation.FieldError("obj", "field", "error message")));
        
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(
                org.mockito.Mockito.mock(org.springframework.core.MethodParameter.class), 
                bindingResult
        );
        
        ResponseEntity<ApiResponse<?>> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Dados inválidos", response.getBody().message());
    }
}
