package com.simon.portfoliotracker.transaction;

import com.simon.portfoliotracker.token.Token;
import com.simon.portfoliotracker.wallet.Wallet;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Wallet wallet;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Token token;
    private Double amount;

    public Transaction(Wallet wallet, Token token, Double amount) {
        this.wallet = wallet;
        this.token = token;
        this.amount = amount;
    }
}
