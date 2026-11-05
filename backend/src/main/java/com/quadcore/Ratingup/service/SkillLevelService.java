package com.quadcore.Ratingup.service;

import com.github.bhlangonijr.chesslib.Side;
import com.quadcore.Ratingup.enums.GameStatus;
import com.quadcore.Ratingup.enums.SkillLevel;
import com.quadcore.Ratingup.enums.TimeControlCategory;
import com.quadcore.Ratingup.model.game.Game;
import com.quadcore.Ratingup.model.game.PlayerRating;
import com.quadcore.Ratingup.repository.GameRepository;
import com.quadcore.Ratingup.repository.PlayerRatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillLevelService {

    static final int WINDOW = 10;              // partidas recentes consideradas
    static final int MIN_GAMES_RATED = 5;      // antes disso o nível é UNRATED
    static final int MIN_GAMES_PERFORMANCE = 5;

    private final GameRepository gameRepository;
    private final PlayerRatingRepository ratingRepository;

    /**
     * Performance rating aproximado: média dos oponentes + 400 * (V - D) / N,
     * sobre as últimas WINDOW partidas da categoria. O nível combina rating e performance.
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void refresh(Long userId, TimeControlCategory category) {
        PlayerRating pr = ratingRepository.findForUpdate(userId, category).orElseThrow();

        List<Game> recent = gameRepository.findRecentFinished(
                userId, category, GameStatus.FINISHED, PageRequest.of(0, WINDOW));

        int n = 0;
        long opponentSum = 0;
        double balance = 0; // soma de (2*score - 1): vitória +1, empate 0, derrota -1
        for (Game g : recent) {
            boolean white = userId.equals(g.getWhitePlayerId());
            Integer opponent = white ? g.getBlackRatingBefore() : g.getWhiteRatingBefore();
            if (opponent == null || g.getResult() == null) continue;
            double score = g.getResult().scoreFor(white ? Side.WHITE : Side.BLACK);
            n++;
            opponentSum += opponent;
            balance += 2 * score - 1;
        }

        Integer performance = null;
        int effective = pr.getRating();
        if (n >= MIN_GAMES_PERFORMANCE) {
            int perf = (int) Math.round(opponentSum / (double) n + 400.0 * balance / n);
            performance = Math.max(100, Math.min(3000, perf));
            effective = (pr.getRating() + performance) / 2;
        }

        pr.setPerformanceRating(performance);
        pr.setSkillLevel(pr.getGamesPlayed() < MIN_GAMES_RATED
                ? SkillLevel.UNRATED
                : SkillLevel.fromRating(effective));
        ratingRepository.save(pr);
    }
}