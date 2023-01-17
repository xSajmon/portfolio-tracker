package com.simon.portfoliotracker.transaction;

import lombok.Data;

import java.util.List;

@Data
class TransactionEvent {
    private final Transaction transaction;
    private final List<TransactionRead> transactions;
}
