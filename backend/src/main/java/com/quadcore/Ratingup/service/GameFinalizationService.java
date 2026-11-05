package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.enums.GameEndReason;
import com.quadcore.Ratingup.enums.GameResult;
import com.quadcore.Ratingup.enums.GameStatus;
import com.quadcore.Ratingup.exception.BusinessRuleViolationException;
import com.quadcore.Ratingup.model.game.Game;
import com.quadcore.Ratingup.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GameFinalizationService {

    private final GameRepository gameRepository;
    private final EloRatingService eloRatingService;
    private final SkillLevelService skillLevelService;
    private final AntiCheatService antiCheatService;

    @Transactional(propagation = Propagation.MANDATORY)
    public void finish(Game game, GameResult result, GameEndReason reason) {
        if (game.getStatus() == GameStatus.FINISHED) {
            throw new BusinessRuleViolationException("A partida já foi finalizada", "GAME_ALREADY_FINISHED");
        }

        game.setStatus(GameStatus.FINISHED);
        game.setResult(result);
        game.setEndReason(reason);
        game.setFinishedAt(LocalDateTime.now());

        // 1) flush antecipado: se outra requisição alterou a partida, o @Version falha
        //    aqui, ANTES de qualquer mudança de rating.
        gameRepository.saveAndFlush(game);

        // 2) rating (linhas travadas com SELECT ... FOR UPDATE)
        eloRatingService.applyResult(game);
        gameRepository.saveAndFlush(game);

        // 3) nível estimado (usa a partida recém-gravada) e 4) anti-cheat
        skillLevelService.refresh(game.getWhitePlayerId(), game.getTimeControlCategory());
        skillLevelService.refresh(game.getBlackPlayerId(), game.getTimeControlCategory());
        antiCheatService.analyze(game);

        gameRepository.save(game);
    }
}