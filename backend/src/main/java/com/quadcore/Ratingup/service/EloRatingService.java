package com.quadcore.Ratingup.service;

import com.github.bhlangonijr.chesslib.Side;
import com.quadcore.Ratingup.enums.TimeControlCategory;
import com.quadcore.Ratingup.model.game.Game;
import com.quadcore.Ratingup.model.game.PlayerRating;
import com.quadcore.Ratingup.repository.PlayerRatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EloRatingService {

    private final PlayerRatingRepository ratingRepository;
    private final EloCalculator calculator;

    /**
     * Calcula e persiste o novo rating dos dois jogadores e grava o antes/variação na partida.
     * Deve rodar dentro da transação que finaliza a partida.
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void applyResult(Game game) {
        TimeControlCategory category = game.getTimeControlCategory();
        Long whiteId = game.getWhitePlayerId();
        Long blackId = game.getBlackPlayerId();

        // Trava as linhas SEMPRE em ordem crescente de id: duas partidas simultâneas
        // com jogadores em comum nunca se travam mutuamente (sem deadlock).
        Map<Long, PlayerRating> locked = new HashMap<>();
        for (Long id : Stream.of(whiteId, blackId).sorted().toList()) {
            ratingRepository.insertIfAbsent(id, category.name(), PlayerRating.DEFAULT_RATING);
            locked.put(id, ratingRepository.findForUpdate(id, category).orElseThrow());
        }
        PlayerRating white = locked.get(whiteId);
        PlayerRating black = locked.get(blackId);

        double whiteScore = game.getResult().scoreFor(Side.WHITE);
        double blackScore = game.getResult().scoreFor(Side.BLACK);

        int whiteBefore = white.getRating();
        int blackBefore = black.getRating();

        // Os dois deltas usam os ratings de ANTES da partida.
        int whiteAfter = Math.max(PlayerRating.MIN_RATING, whiteBefore
                + calculator.delta(whiteBefore, blackBefore, whiteScore, white.getGamesPlayed()));
        int blackAfter = Math.max(PlayerRating.MIN_RATING, blackBefore
                + calculator.delta(blackBefore, whiteBefore, blackScore, black.getGamesPlayed()));

        white.recordGame(whiteAfter, whiteScore);
        black.recordGame(blackAfter, blackScore);
        ratingRepository.saveAll(java.util.List.of(white, black));

        game.setWhiteRatingBefore(whiteBefore);
        game.setBlackRatingBefore(blackBefore);
        game.setWhiteRatingChange(whiteAfter - whiteBefore);
        game.setBlackRatingChange(blackAfter - blackBefore);
    }
}