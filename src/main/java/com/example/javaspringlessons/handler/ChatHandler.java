package com.example.javaspringlessons.handler;

import com.example.javaspringlessons.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {

    private final SessionRepository repository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
      repository.addSession(session);
        System.out.println("connected: " +session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        repository.sendToAll(message.getPayload());
        System.out.println(message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        repository.removeSession(session);
        System.out.println("closed: " +session.getId());
    }
}
