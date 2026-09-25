package com.quadcore.Ratingup.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.quadcore.Ratingup.dto.response.StandardError;
import com.quadcore.Ratingup.exception.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Should handle EntityNotFoundException")
    void testEntityNotFound() {
        ResponseEntity<StandardError> response = handler.handleNotFound(new EntityNotFoundException("Not found"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not found", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle DataIntegrityViolationException")
    void testDataIntegrity() {
        ResponseEntity<StandardError> response = handler.handleDataIntegrity(new DataIntegrityViolationException("Conflict"));
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Violação de integridade de dados", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle JWT Exceptions")
    void testJwt() {
        ResponseEntity<StandardError> response = handler.handleJwt(new JWTVerificationException("Invalid token"));
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token inválido ou expirado", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle AuthenticationException")
    void testAuthentication() {
        AuthenticationException ex = new AuthenticationException("Auth failed") {};
        ResponseEntity<StandardError> response = handler.handleAuthentication(ex);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Não autenticado", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle AccessDeniedException")
    void testAccessDenied() {
        ResponseEntity<StandardError> response = handler.handleAccessDenied(new AccessDeniedException("Denied"));
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Acesso negado", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle MailException")
    void testMail() {
        MailException ex = new MailException("Mail failed") {};
        ResponseEntity<StandardError> response = handler.handleMail(ex);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Falha ao enviar e-mail. Tente novamente mais tarde.", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle RuntimeException")
    void testRuntime() {
        ResponseEntity<StandardError> response = handler.handleRuntime(new RuntimeException("Runtime error"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro interno no servidor", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle general Exception")
    void testGeneral() {
        ResponseEntity<StandardError> response = handler.handleGeneral(new Exception("General error"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro interno no servidor", response.getBody().message());
    }
    
    @Test
    @DisplayName("Should handle FieldValidationException")
    void testValidation() {
        FieldValidationException ex = new FieldValidationException("field1", "Invalid data");
        ResponseEntity<StandardError> response = handler.handleFieldValidation(ex);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("Dados inválidos", response.getBody().message());
        Map<String, String> errors = (Map<String, String>) response.getBody().errors();
        assertEquals("Invalid data", errors.get("field1"));
    }
    
    @Test
    @DisplayName("Should handle ConflictException")
    void testDuplicateField() {
        ConflictException ex = new ConflictException("Campos duplicados", List.of("Error 1"));
        ResponseEntity<StandardError> response = handler.handleConflict(ex);
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
        
        ResponseEntity<StandardError> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Dados inválidos", response.getBody().message());
    }
}
