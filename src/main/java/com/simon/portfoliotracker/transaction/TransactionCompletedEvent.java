package com.simon.portfoliotracker.transaction;

import org.springframework.context.ApplicationEvent;

import java.util.List;

public class TransactionCompletedEvent extends TransactionEvent {

    public TransactionCompletedEvent(Transaction transaction, List<TransactionRead> transactions) {
        super(transaction, transactions);
    }
}
