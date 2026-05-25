package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.profile.AvatarResponseDTO;
import com.quadcore.Ratingup.dto.profile.AvatarSelectionDTO;
import com.quadcore.Ratingup.dto.response.ApiResponse;
import com.quadcore.Ratingup.service.AvatarService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Avatar")
@RestController
@RequestMapping("/avatar")
public class AvatarController {
    //Controller para gerenciar os endpoints relacionados ao avatar do usuário
    @Autowired
    private AvatarService avatarService;

    @GetMapping("/avatar-list")
    public ResponseEntity<ApiResponse<List<AvatarResponseDTO>>> showAvatarList(){
        List<AvatarResponseDTO> list = avatarService.listAvaliableImages();
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de usuários recebida", list));
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse<?>> updateAvatar(@RequestBody @Valid AvatarSelectionDTO dto){
        avatarService.selectAvatar(dto.imageName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Imagem escolhida com sucesso", dto));
    }
}
