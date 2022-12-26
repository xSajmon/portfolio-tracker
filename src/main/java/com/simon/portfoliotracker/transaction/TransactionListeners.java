package com.simon.portfoliotracker.transaction;

import com.simon.portfoliotracker.WebSocketBrokerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionListeners {

    private final WebSocketBrokerService socketService;

    @EventListener
    public void onTransactionAddedListener(TransactionAddedEvent event){
        socketService.sendTransactions(event.getTransactions());
    }
}
