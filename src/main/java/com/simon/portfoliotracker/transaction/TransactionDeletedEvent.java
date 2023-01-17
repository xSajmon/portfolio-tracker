package com.simon.portfoliotracker.transaction;

import lombok.Data;

import java.util.List;


public class TransactionDeletedEvent extends TransactionEvent {
    public TransactionDeletedEvent(Transaction transaction, List<TransactionRead> transactions) {
        super(transaction, transactions);
    }
}
