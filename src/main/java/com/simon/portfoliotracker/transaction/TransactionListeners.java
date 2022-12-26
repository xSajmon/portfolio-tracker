package com.simon.portfoliotracker.transaction;

import com.simon.portfoliotracker.WebSocketBrokerService;
import com.simon.portfoliotracker.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionListeners {

    private final WebSocketBrokerService socketService;
    private final WalletService walletService;

    @EventListener
    public void onTransactionAddedListener(TransactionAddedEvent event){
        walletService.updateWallet(event.getTransaction());
        socketService.sendTransactions(event.getTransactions());
    }
}
