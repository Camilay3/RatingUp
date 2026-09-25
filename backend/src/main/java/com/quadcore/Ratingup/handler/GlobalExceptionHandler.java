package com.quadcore.Ratingup.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.quadcore.Ratingup.dto.response.StandardError;
import com.quadcore.Ratingup.exception.*;
import io.minio.errors.ErrorResponseException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private StandardError buildError(HttpStatus status, String code, String message, Object errors) {
        return new StandardError(LocalDateTime.now(), status.value(), status.getReasonPhrase(), code, message, errors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getErrorCode(), ex.getMessage(), null));
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<StandardError> handleBusinessRule(BusinessRuleViolationException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(buildError(HttpStatus.UNPROCESSABLE_ENTITY, ex.getErrorCode(), ex.getMessage(), null));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<StandardError> handleConflict(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, ex.getErrorCode(), ex.getMessage(), ex.getConflictDetails()));
    }

    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<StandardError> handleUnauthorizedOperation(UnauthorizedOperationException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(buildError(HttpStatus.FORBIDDEN, ex.getErrorCode(), ex.getMessage(), null));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<StandardError> handleDomainException(DomainException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, ex.getErrorCode(), ex.getMessage(), null));
    }

    @ExceptionHandler(FieldValidationException.class)
    public ResponseEntity<StandardError> handleFieldValidation(FieldValidationException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        errors.put(ex.getField(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(buildError(HttpStatus.UNPROCESSABLE_ENTITY, ex.getErrorCode(), "Dados inválidos", errors));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleBadRequest(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(e ->
                errors.computeIfAbsent(e.getField(), k -> new ArrayList<>()).add(e.getDefaultMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Dados inválidos", errors));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> handleDataIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, "DATA_INTEGRITY_VIOLATION", "Violação de integridade de dados", null));
    }

    @ExceptionHandler({JWTVerificationException.class})
    public ResponseEntity<StandardError> handleJwt(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(buildError(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "Token inválido ou expirado", null));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<StandardError> handleAuthentication(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(buildError(HttpStatus.UNAUTHORIZED, "UNAUTHENTICATED", "Não autenticado", null));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardError> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(buildError(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "Acesso negado", null));
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<StandardError> handleMail(MailException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(buildError(HttpStatus.SERVICE_UNAVAILABLE, "MAIL_ERROR", "Falha ao enviar e-mail. Tente novamente mais tarde.", null));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<StandardError> handleRuntime(RuntimeException ex) {
        ex.printStackTrace(); // Logar o erro genérico para investigar vazamentos
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Erro interno no servidor", null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleGeneral(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Erro interno no servidor", null));
    }

    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<StandardError> handleMinioError(ErrorResponseException ex) {
        String errorCode = ex.errorResponse().code();
        return switch (errorCode) {
            case "NoSuchKey" -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(buildError(HttpStatus.NOT_FOUND, "IMAGE_NOT_FOUND", "Imagem não encontrada", null));
            case "NoSuchBucket" -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(buildError(HttpStatus.NOT_FOUND, "BUCKET_NOT_FOUND", "Bucket não encontrado", null));
            case "AccessDenied" -> ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(buildError(HttpStatus.FORBIDDEN, "STORAGE_ACCESS_DENIED", "Sem permissão para acessar o armazenamento", null));
            default -> ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(buildError(HttpStatus.BAD_GATEWAY, "STORAGE_ERROR", "Erro no armazenamento: " + ex.getMessage(), null));
        };
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> handleInvalidEnum(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException ife && ife.getTargetType().isEnum()) {
            String valoresAceitos = Arrays.stream(ife.getTargetType().getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(buildError(HttpStatus.BAD_REQUEST, "INVALID_ENUM_VALUE", 
                            "Valor inválido: '" + ife.getValue() + "'. Valores aceitos para "
                                    + ife.getTargetType().getSimpleName() + ": " + valoresAceitos, null));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "Requisição inválida", null));
    }
}
