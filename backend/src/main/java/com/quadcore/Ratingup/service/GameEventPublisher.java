package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.dto.game.GameEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameEventPublisher {
    private final SimpMessagingTemplate messagingTemplate;

    public void publish(Long gameId, GameEventDTO event) {
        messagingTemplate.convertAndSend("/topic/game/" + gameId, event);
    }
}
