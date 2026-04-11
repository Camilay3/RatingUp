package com.quadcore.Ratingup.controller;


import com.quadcore.Ratingup.dto.profile.PhaseUpdateDTO;
import com.quadcore.Ratingup.dto.profile.ProfileResponseDTO;
import com.quadcore.Ratingup.mapper.UserMapper;
import com.quadcore.Ratingup.model.User;
import com.quadcore.Ratingup.service.LessonService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/fase")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PutMapping("/{id}/resultado")
    public ResponseEntity<ProfileResponseDTO> atualizaFaseAtual(@PathVariable Long id, @Valid @RequestBody PhaseUpdateDTO dto) {
        User data = new User();
        data.setFaseAtual(dto.faseAtual());

        Optional<User> userAtualizado = lessonService.atualizaFaseAtual(id, data);

        return userAtualizado
                .map(user -> ResponseEntity.ok(UserMapper.toResponseDTO(user)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhaseUpdateDTO> buscaFaseAtual(@PathVariable Long id) {
        Optional<User> user = lessonService.buscaFaseAtual(id);

        return user
                .map(value -> ResponseEntity.ok(UserMapper.toPhaseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
