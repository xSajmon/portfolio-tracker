package com.simon.portfoliotracker;

import com.simon.portfoliotracker.token.TokenService;
import com.simon.portfoliotracker.transaction.Transaction;
import com.simon.portfoliotracker.transaction.TransactionRead;
import com.simon.portfoliotracker.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class WebSocketBrokerService {

    private final TokenService tokenService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendTransactions(List<TransactionRead> transactions){
        simpMessagingTemplate.convertAndSend("/topic/transactions", transactions);
    }
    @Scheduled(fixedRate = 5000)
    public void publishPrices(){
        System.out.println(tokenService.fetchCryptoPrices().stream().findFirst());
        simpMessagingTemplate.convertAndSend("/topic/crypto-price", tokenService.fetchCryptoPrices());
    }
}
