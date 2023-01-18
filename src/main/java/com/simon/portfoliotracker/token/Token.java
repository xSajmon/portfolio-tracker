package com.simon.portfoliotracker.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.simon.portfoliotracker.ownedToken.OwnedToken;
import com.simon.portfoliotracker.transaction.Transaction;
import com.simon.portfoliotracker.wallet.Wallet;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private String name;
    private String symbol;
    @JsonProperty("id")
    private String urlName;


    @JsonIgnore
    @OneToOne(mappedBy = "token")
    private OwnedToken ownedToken;

    @JsonIgnore
    @OneToMany(mappedBy = "token")
    private List<Transaction> transaction;


    @JsonProperty("priceUsd")
    @Transient
    private Double currentPrice;

    public Token(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public Double getCurrentPrice() {
        if (currentPrice != null) {
            currentPrice = new BigDecimal(Double.toString(currentPrice))
                    .setScale(3, RoundingMode.HALF_UP)
                    .doubleValue();
        }
        return currentPrice;
    }
}
