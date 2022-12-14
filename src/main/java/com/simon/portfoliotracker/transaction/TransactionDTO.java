package com.simon.portfoliotracker.transaction;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Positive;

@Getter
public class TransactionDTO {

    private Long walletId;
    @NotBlank(message = "Please choose a token.")
    private String token;
    @Positive(message = "Incorrect amount.")
    private Double amount;
    private String type;
}
