package com.simon.portfoliotracker;

import com.simon.portfoliotracker.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final TransactionService transactionService;
    private final SimpMessagingTemplate template;

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event){
        String topic = (String) event.getMessage().getHeaders().get("simpDestination");
        if(topic.equals("/topic/transactions")){
            template.convertAndSend(topic, transactionService.getTransactions());
        }

    }
}
