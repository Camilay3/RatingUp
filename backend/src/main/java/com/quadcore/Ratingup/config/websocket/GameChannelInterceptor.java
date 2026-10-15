package com.quadcore.Ratingup.config.websocket;

import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.service.GameService;

import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GameChannelInterceptor implements ChannelInterceptor {
    private static final Pattern GAME_TOPIC = Pattern.compile("^/topic/game/(\\d+)$");
    private final GameService gameService;

    public GameChannelInterceptor(@Lazy GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public @Nullable Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor acc = StompHeaderAccessor.wrap(message);

        if (StompCommand.SEND.equals(acc.getCommand())) {
            throw new MessagingException("Envie um lance pela API REST");
        }
        if (StompCommand.SUBSCRIBE.equals(acc.getCommand())) {
            Long gameId =extractGamerId(acc.getDestination());
            User user = extractUser(acc);
            if (gameId == null || user == null || !gameService.isParticipant(gameId, user.getId())) {
                throw new MessagingException("Subscrição negada");
            }
        }
        return message;
    }

    private Long extractGamerId(String destination) {
        if (destination == null) return null;
        Matcher m = GAME_TOPIC.matcher(destination);
        return m.matches() ? Long.valueOf(m.group(1)) : null;
    }

    private User extractUser(StompHeaderAccessor acc) {
        if (acc.getUser() instanceof Authentication auth && auth.getPrincipal() instanceof User u) {
            return u;
        }
        return null;
    }
}
