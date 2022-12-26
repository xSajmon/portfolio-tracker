package com.simon.portfoliotracker.transaction;

import lombok.Data;

import java.util.List;

@Data
public class TransactionAddedEvent {
    private final List<TransactionRead> transactions;
}
