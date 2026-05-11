package com.quadcore.Ratingup.controller;


import com.quadcore.Ratingup.dto.profile.PasswordChangeDTO;
import com.quadcore.Ratingup.dto.profile.PasswordResetDTO;
import com.quadcore.Ratingup.dto.profile.PasswordResetRequestDTO;
import com.quadcore.Ratingup.dto.response.ApiResponse;
import com.quadcore.Ratingup.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<?>> changePassword(@RequestBody PasswordChangeDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        userService.changePassword(userDetails.getUsername(), dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Senha alterada com sucesso!", null));
    }
}
