package com.simon.portfoliotracker;

import com.simon.portfoliotracker.token.Observer;
import com.simon.portfoliotracker.token.TokenDto;
import com.simon.portfoliotracker.transaction.TransactionRead;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebSocketBrokerService implements Observer {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendTransactions(List<TransactionRead> transactions){
        simpMessagingTemplate.convertAndSend("/topic/transactions", transactions);
    }
    public void publishPrices(List<TokenDto> tokens){
        simpMessagingTemplate.convertAndSend("/topic/crypto-price", tokens);
    }
    @Override
    public void update(List<TokenDto> tokens) {
        System.out.println(tokens.size());
        System.out.println(tokens);
        publishPrices(tokens);
    }
}
