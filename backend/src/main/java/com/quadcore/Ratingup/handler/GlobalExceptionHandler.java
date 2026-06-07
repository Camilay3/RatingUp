package com.quadcore.Ratingup.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.quadcore.Ratingup.dto.response.ApiResponse;
import io.jsonwebtoken.JwtException;
import io.minio.errors.ErrorResponseException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleBadRequest(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(e -> erros.put(e.getField(), e.getDefaultMessage()));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Dados inválidos", erros));
    }

    @ExceptionHandler(DuplicateFieldException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateFieldException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("CONFLICT", ex.getErros()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(false, "Violação de integridade de dados", null));
    }

    @ExceptionHandler({JwtException.class, JWTVerificationException.class})
    public ResponseEntity<ApiResponse<?>> handleJwt(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(false, "Token inválido ou expirado", null));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthentication(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(false, "Não autenticado", null));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>(false, "Acesso negado", null));
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<ApiResponse<?>> handleMail(MailException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ApiResponse<>(false, "Falha ao enviar e-mail. Tente novamente mais tarde.", null));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntime(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneral(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Erro interno no servidor", null));
    }

    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<ApiResponse<?>> handleMinioError (ErrorResponseException ex) {
        String errorCode = ex.errorResponse().code();

        return switch (errorCode) {
            case "NoSuchKey" -> ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Imagem não encontrada", null));

            case "NoSuchBucket" -> ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Bucket não encontrado", null));

            case "AccessDenied" -> ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, "Sem permissão para acessar o armazenamento", null));

            default -> ResponseEntity
                    .status(HttpStatus.BAD_GATEWAY)
                    .body(new ApiResponse<>(false, "Erro no armazenamento: " + ex.getMessage(), null));
        };
    }
}
