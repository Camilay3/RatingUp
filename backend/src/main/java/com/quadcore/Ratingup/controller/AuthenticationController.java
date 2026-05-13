package com.quadcore.Ratingup.controller;


import com.quadcore.Ratingup.dto.profile.*;
import com.quadcore.Ratingup.dto.response.ApiResponse;
import com.quadcore.Ratingup.service.UserService;
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

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController (UserService userService){
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> loginUser(
            @Valid @RequestBody LoginRequestDTO dto,
            HttpServletResponse response
    ) {

        String token = userService.loginUser(
                dto.email(),
                dto.password()
        );

        ResponseCookie cookie = ResponseCookie
                .from("token", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
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
        ResponseCookie cookie = ResponseCookie
                .from("token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(new ApiResponse<>(true,"Usuário deslogado com sucesso",null));
    }

    @PostMapping("/recover-password")
    public ResponseEntity<ApiResponse<?>> recoverRequest(@RequestBody PasswordResetRequestDTO dto){
        userService.PasswordRecoverRequest(dto.email());
        return ResponseEntity.ok(new ApiResponse<>(true,"Email de recuperação enviado", null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<?>> resetPassword(@RequestBody PasswordResetDTO dto){
        userService.resetPassword(dto);
        return ResponseEntity.ok(new ApiResponse<>(true,"Senha redefinida com sucesso!",null));
    }
    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<?>> changePassword(@RequestBody PasswordChangeDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        userService.changePassword(userDetails.getUsername(), dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Senha alterada com sucesso!", null));
    }
}
