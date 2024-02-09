package com.example.javaspringlessons.job;

import com.example.javaspringlessons.repository.WebSocketSessionRepository;
import com.google.common.collect.Iterators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
@Slf4j
public class SendPromosToActiveUsers {

    private final WebSocketSessionRepository webSocketSessionRepository;

    private final Iterator<String> promos = Iterators.cycle("-25% discount","Buy 2 Pay 1", "Free delivery");

    public SendPromosToActiveUsers(WebSocketSessionRepository webSocketSessionRepository) {
        this.webSocketSessionRepository = webSocketSessionRepository;
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void sendPromos(){
        log.info("sending special offers...");
        webSocketSessionRepository.sendToAll(promos.next());
    }

}
