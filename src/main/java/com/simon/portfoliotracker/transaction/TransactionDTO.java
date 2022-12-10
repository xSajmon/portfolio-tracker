package com.simon.portfoliotracker.transaction;

import lombok.Getter;

@Getter
public class TransactionDTO {

    private Long walletId;
    private String token;
    private Double amount;
}
