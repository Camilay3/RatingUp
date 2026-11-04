package com.quadcore.Ratingup.controller;

import com.quadcore.Ratingup.dto.game.*;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.service.GameService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Partidas", description = "Endpoints do modo multiplayer")
@RequestMapping("/games")
@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping
    public ResponseEntity<GameStateDTO> create(
            @AuthenticationPrincipal User logged,
            @RequestBody(required = false) @Valid CreateGameRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.createGame(logged.getId(), dto));
    }

    @GetMapping("/open")
    public ResponseEntity<List<GameSummaryDTO>> listOpen(@AuthenticationPrincipal User logged) {
        return ResponseEntity.ok(gameService.listOpenGames(logged.getId()));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<GameSummaryDTO>> listMine(@AuthenticationPrincipal User logged) {
        return ResponseEntity.ok(gameService.listMyGames(logged.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameStateDTO> getState(
            @AuthenticationPrincipal User logged,
            @PathVariable Long id) {
        return ResponseEntity.ok(gameService.getState(id, logged.getId()));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<GameStateDTO> join(
            @AuthenticationPrincipal User logged,
            @PathVariable Long id) {
        return ResponseEntity.ok(gameService.joinGame(id, logged.getId()));
    }

    @PostMapping("/{id}/moves")
    public ResponseEntity<GameEventDTO> move(
            @AuthenticationPrincipal User logged,
            @PathVariable Long id,
            @RequestBody @Valid GameMoveResquestDTO dto) {
        return ResponseEntity.ok(gameService.makeMove(id, logged.getId(), dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(
            @AuthenticationPrincipal User logged,
            @PathVariable Long id) {
        gameService.cancelGame(id, logged.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/resign")
    public ResponseEntity<GameEventDTO> resign(
            @AuthenticationPrincipal User logged,
            @PathVariable Long id) {
        return ResponseEntity.ok(gameService.resign(id, logged.getId()));
    }

    @GetMapping("/ratings/me")
    public ResponseEntity<List<PlayerRatingDTO>> myRatings(@AuthenticationPrincipal User logged) {
        return ResponseEntity.ok(gameService.listMyRatings(logged.getId()));
    }

    @PostMapping("/{id}/timeout")
    public ResponseEntity<GameEventDTO> timeout(
            @AuthenticationPrincipal User logged,
            @PathVariable Long id) {
        return ResponseEntity.ok(gameService.claimTimeout(id, logged.getId()));
    }
}