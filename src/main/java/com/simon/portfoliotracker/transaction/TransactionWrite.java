package com.simon.portfoliotracker.transaction;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Positive;

@Getter
@Setter
public class TransactionWrite {

    private Long walletId;
    @NotBlank(message = "Please choose a token.")
    private String token;
    @Positive(message = "Incorrect amount.")
    private Double amount;
}
