package com.quadcore.Ratingup.controller;


import com.quadcore.Ratingup.dto.profile.*;
import com.quadcore.Ratingup.dto.response.ApiResponse;
import com.quadcore.Ratingup.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final ConcurrentHashMap<String, Long> rateLimitMap = new ConcurrentHashMap<>();

    private boolean isRateLimited(String ip) {
        long now = System.currentTimeMillis();
        long window = 60000; // 1 minute
        rateLimitMap.values().removeIf(time -> now - time > window);
        long attempts = rateLimitMap.entrySet().stream().filter(e -> e.getKey().startsWith(ip + "_")).count();
        if (attempts >= 5) {
            return true;
        }
        rateLimitMap.put(ip + "_" + now + "_" + UUID.randomUUID(), now);
        return false;
    }

    public AuthenticationController (UserService userService){
        this.userService = userService;
    }

    @Operation(summary = "fazer o login",description = "faz o login no sitema com um usuário existente")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> loginUser(
            @Valid @RequestBody LoginRequestDTO dto,
            HttpServletResponse response
    ) {
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                userService.loginUser(
                        dto.email(),
                        dto.password())
                        .toString()
        );
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Usuário realizou login com sucesso",
                        null
                )
        );
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logoutUser(HttpServletResponse response){
        response.addHeader(HttpHeaders.SET_COOKIE, userService.logoutUser().toString());
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Usuário deslogado com sucesso",
                null));
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Void> validateToken(@RequestParam String token, HttpServletRequest request){
        if (isRateLimited(request.getRemoteAddr())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        ResponseCookie cookie = userService.validateResetToken(token);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @PostMapping("/recover-password")
    public ResponseEntity<ApiResponse<?>> recoverRequest(@RequestBody @Valid PasswordResetRequestDTO dto, HttpServletRequest request){
        if (isRateLimited(request.getRemoteAddr())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new ApiResponse<>(false, "Muitas requisições. Tente novamente mais tarde.", null));
        }
        userService.passwordRecoverRequest(dto.email());
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Email de recuperação enviado",
                        null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<?>> resetPassword(@RequestBody @Valid PasswordResetDTO dto){
        userService.resetPassword(dto.newPassword());
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Senha redefinida com sucesso!",
                        null));
    }
    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<?>> changePassword(@RequestBody @Valid  PasswordChangeDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        userService.changePassword(userDetails.getUsername(), dto);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Senha alterada com sucesso!",
                        null));
    }
}
