package com.simon.portfoliotracker.token;

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
    private Long id;
    private String name;
    private String symbol;
    private Double currentPrice;

    @OneToOne(mappedBy = "token")
    private OwnedToken ownedToken;

    @OneToOne(mappedBy = "token")
    private Transaction transaction;

    public Token(String name, String symbol, Double currentPrice) {
        this.name = name;
        this.symbol = symbol;
        this.currentPrice = currentPrice;
    }
}
