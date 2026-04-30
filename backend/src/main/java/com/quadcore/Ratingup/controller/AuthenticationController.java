package com.quadcore.Ratingup.controller;


import com.quadcore.Ratingup.dto.profile.PasswordResetDTO;
import com.quadcore.Ratingup.dto.profile.PasswordResetRequestDTO;
import com.quadcore.Ratingup.dto.response.ApiResponse;
import com.quadcore.Ratingup.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController (UserService userService){
        this.userService = userService;
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
}
