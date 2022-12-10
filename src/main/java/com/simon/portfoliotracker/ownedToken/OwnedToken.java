package com.simon.portfoliotracker.ownedToken;

import com.simon.portfoliotracker.token.Token;
import com.simon.portfoliotracker.wallet.Wallet;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class OwnedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Wallet wallet;
    @OneToOne
    private Token token;
    private Double amount;

    private Double price;

    public OwnedToken(Wallet wallet, Token token, Double amount, Double price) {
        this.wallet = wallet;
        this.token = token;
        this.amount = amount;
        this.price = price;
    }
}
