package com.simon.portfoliotracker.transaction;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;



public class TransactionAddedEvent extends TransactionEvent {
    public TransactionAddedEvent(Transaction transaction, List<TransactionRead> transactions) {
        super(transaction, transactions);
    }
}
