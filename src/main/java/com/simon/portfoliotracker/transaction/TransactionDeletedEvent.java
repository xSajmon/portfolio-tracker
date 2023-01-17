package com.simon.portfoliotracker.transaction;

import lombok.Data;

import java.util.List;

@Data
public class TransactionDeletedEvent {
    private final Transaction transaction;
    private final List<TransactionRead> transactions;
}
