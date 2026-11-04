package com.quadcore.Ratingup.service;

import com.github.bhlangonijr.chesslib.Side;
import com.quadcore.Ratingup.enums.TimeControlCategory;
import com.quadcore.Ratingup.model.game.Game;
import com.quadcore.Ratingup.model.game.GameReviewFlag;
import com.quadcore.Ratingup.model.game.PlayerRating;
import com.quadcore.Ratingup.repository.GameReviewFlagRepository;
import com.quadcore.Ratingup.repository.PlayerRatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Heurísticas básicas. Elas SINALIZAM para revisão humana; nunca punem automaticamente.
 * O tempo por lance é medido no servidor, então inclui a latência de rede.
 */
@Service
@RequiredArgsConstructor
public class AntiCheatService {

    static final int OPENING_MOVES_TO_SKIP = 4;   // teoria/premoves de abertura
    static final int MIN_ANALYZED_MOVES = 15;
    static final double UNIFORM_CV_LIMIT = 0.20;  // humanos têm CV bem acima disso
    static final double UNIFORM_MIN_MEAN_MS = 250;
    static final int PERFORMANCE_SPIKE = 350;     // performance - rating
    static final int MIN_GAMES_FOR_SPIKE = 10;
    static final int FLAG_THRESHOLD = 2;          // pontos para abrir revisão

    private final GameReviewFlagRepository flagRepository;
    private final PlayerRatingRepository ratingRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public void analyze(Game game) {
        List<Long> allTimes = parseTimes(game.getMoveTimesMs());
        boolean flagged = false;

        for (Side side : Side.values()) {
            Long playerId = side == Side.WHITE ? game.getWhitePlayerId() : game.getBlackPlayerId();
            Evaluation ev = evaluate(game.getTimeControlCategory(), playerId, timesOf(allTimes, side));

            if (ev.score() >= FLAG_THRESHOLD) {
                flagged = true;
                if (!flagRepository.existsByGameIdAndUserId(game.getId(), playerId)) {
                    GameReviewFlag flag = new GameReviewFlag();
                    flag.setGameId(game.getId());
                    flag.setUserId(playerId);
                    flag.setScore(ev.score());
                    flag.setReasons(String.join("; ", ev.reasons()));
                    flagRepository.save(flag);
                }
            }
        }
        if (flagged) game.setFlaggedForReview(true);
    }

    private Evaluation evaluate(TimeControlCategory category, Long playerId, List<Long> times) {
        int score = 0;
        List<String> reasons = new ArrayList<>();

        List<Long> sample = times.size() > OPENING_MOVES_TO_SKIP
                ? times.subList(OPENING_MOVES_TO_SKIP, times.size()) : List.of();

        if (sample.size() >= MIN_ANALYZED_MOVES) {
            double mean = sample.stream().mapToLong(Long::longValue).average().orElse(0);
            double variance = sample.stream().mapToDouble(t -> Math.pow(t - mean, 2)).average().orElse(0);
            double cv = mean > 0 ? Math.sqrt(variance) / mean : 0;

            if (mean < fastThresholdMs(category)) {
                score += 1;
                reasons.add(String.format(Locale.ROOT, "FAST_AVERAGE(mean=%.0fms)", mean));
            }
            if (mean >= UNIFORM_MIN_MEAN_MS && cv < UNIFORM_CV_LIMIT) {
                score += 2;
                reasons.add(String.format(Locale.ROOT, "UNIFORM_PACE(cv=%.2f,mean=%.0fms)", cv, mean));
            }
        }

        PlayerRating pr = ratingRepository.findByUserIdAndCategory(playerId, category).orElse(null);
        if (pr != null && pr.getGamesPlayed() >= MIN_GAMES_FOR_SPIKE && pr.getPerformanceRating() != null
                && pr.getPerformanceRating() - pr.getRating() >= PERFORMANCE_SPIKE) {
            score += 1;
            reasons.add("PERFORMANCE_SPIKE(perf=" + pr.getPerformanceRating() + ",rating=" + pr.getRating() + ")");
        }
        return new Evaluation(score, reasons);
    }

    private long fastThresholdMs(TimeControlCategory category) {
        return switch (category) {
            case BULLET -> 200;
            case BLITZ -> 700;
            case RAPID -> 1500;
            case CLASSICAL -> 3000;
        };
    }

    private List<Long> parseTimes(String csv) {
        if (csv == null || csv.isBlank()) return List.of();
        return Arrays.stream(csv.split(",")).map(String::trim).map(Long::valueOf).toList();
    }

    /** Índice par = lance das brancas. */
    private List<Long> timesOf(List<Long> all, Side side) {
        List<Long> result = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            if ((i % 2 == 0) == (side == Side.WHITE)) result.add(all.get(i));
        }
        return result;
    }

    private record Evaluation(int score, List<String> reasons) {}
}