package com.quadcore.Ratingup.service;

import org.springframework.stereotype.Component;

@Component
public class EloCalculator {

    /** K estilo FIDE: 40 para novatos (<30 partidas), 10 acima de 2400, 20 no restante. */
    public int kFactor(int gamesPlayed, int rating) {
        if (gamesPlayed < 30) return 40;
        if (rating >= 2400) return 10;
        return 20;
    }

    public double expectedScore(int rating, int opponentRating) {
        return 1.0 / (1.0 + Math.pow(10.0, (opponentRating - rating) / 400.0));
    }

    /** Variação de rating (positiva ou negativa) para uma partida. */
    public int delta(int rating, int opponentRating, double score, int gamesPlayed) {
        double k = kFactor(gamesPlayed, rating);
        return (int) Math.round(k * (score - expectedScore(rating, opponentRating)));
    }
}