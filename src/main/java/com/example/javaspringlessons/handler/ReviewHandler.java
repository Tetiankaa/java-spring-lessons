package com.example.javaspringlessons.handler;

import com.example.javaspringlessons.repository.WebSocketSessionRepository;
import com.example.javaspringlessons.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
public class ReviewHandler extends TextWebSocketHandler {

    private final ReviewService reviewService;
    private final WebSocketSessionRepository webSocketSessionRepository;

    public ReviewHandler(ReviewService reviewService, WebSocketSessionRepository webSocketSessionRepository) {

        this.reviewService = reviewService;
        this.webSocketSessionRepository = webSocketSessionRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("connection established");
        webSocketSessionRepository.addSession(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message){
        String msg = message.getPayload();

        reviewService.saveReview(255,msg);

        log.info("New review {}: ", msg);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

        log.info("connection closed");
        webSocketSessionRepository.removeSession(session);
    }
}
