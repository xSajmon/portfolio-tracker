package com.simon.portfoliotracker;

import com.simon.portfoliotracker.token.TokenService;
import com.simon.portfoliotracker.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class WebSocketBrokerService {

    private final TokenService tokenService;
    private final TransactionService transactionService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @Scheduled(fixedRate = 5000)
    public void sendTransactions(){
        simpMessagingTemplate.convertAndSend("/topic/transactions", transactionService.getTransactions());
    }
    @Scheduled(fixedRate = 5000)
    public void publishPrices(){
        System.out.println(tokenService.fetchCryptoPrices().stream().findFirst());
        simpMessagingTemplate.convertAndSend("/topic/crypto-price", tokenService.fetchCryptoPrices());
    }
}
