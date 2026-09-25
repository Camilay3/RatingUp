package com.quadcore.Ratingup.handler;
import com.quadcore.Ratingup.exception.FieldValidationException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerMoreTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    enum DummyEnum { A, B }

    @Test
    @DisplayName("Should handle HttpMessageNotReadableException with InvalidFormatException")
    void testInvalidEnum() {
        InvalidFormatException ife = mock(InvalidFormatException.class);
        when(ife.getTargetType()).thenReturn((Class) DummyEnum.class);
        when(ife.getValue()).thenReturn("C");

        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("error", ife, null);

        var response = handler.handleInvalidEnum(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Valor inválido: 'C'. Valores aceitos para DummyEnum: A, B", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle generic HttpMessageNotReadableException")
    void testGenericMessageNotReadable() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("error", null, null);

        var response = handler.handleInvalidEnum(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Requisição inválida", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle DataIntegrityViolationException")
    void testDataIntegrity() {
        org.springframework.dao.DataIntegrityViolationException ex = new org.springframework.dao.DataIntegrityViolationException("error");
        var response = handler.handleDataIntegrity(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle MailException")
    void testMailException() {
        org.springframework.mail.MailException ex = new org.springframework.mail.MailException("error") {};
        var response = handler.handleMail(ex);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle ValidationException")
    void testValidationException() {
        FieldValidationException ex = new FieldValidationException("field", "msg");
        var response = handler.handleFieldValidation(ex);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle AuthenticationException")
    void testAuthenticationException() {
        org.springframework.security.core.AuthenticationException ex = new org.springframework.security.core.AuthenticationException("error") {};
        var response = handler.handleAuthentication(ex);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
