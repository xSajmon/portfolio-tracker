package com.simon.portfoliotracker.transaction;

import com.simon.portfoliotracker.token.Token;
import com.simon.portfoliotracker.wallet.Wallet;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

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
    private Double buyingPrice;
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    public Transaction(Wallet wallet, Token token, Double amount, TransactionType transactionType, Double buyingPrice) {
        this.wallet = wallet;
        this.token = token;
        this.amount = amount;
        this.transactionType = transactionType;
        this.buyingPrice = buyingPrice;
    }

    @PrePersist
    private void onCreate(){
        date = LocalDateTime.now();
    }
}
