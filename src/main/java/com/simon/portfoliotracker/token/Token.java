package com.simon.portfoliotracker.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.simon.portfoliotracker.ownedToken.OwnedToken;
import com.simon.portfoliotracker.transaction.Transaction;
import com.simon.portfoliotracker.wallet.Wallet;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private String name;
    private String symbol;

    @OneToOne(mappedBy = "token")
    private OwnedToken ownedToken;

    @OneToOne(mappedBy = "token")
    private Transaction transaction;


    @JsonProperty("priceUsd")
    @Transient
    private Double currentPrice;

    public Token(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }
}
