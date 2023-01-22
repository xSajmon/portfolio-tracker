package com.simon.portfoliotracker.transaction;

import com.simon.portfoliotracker.token.Token;
import com.simon.portfoliotracker.wallet.Wallet;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Wallet wallet;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Token token;
    private Double amount;
    private Double buyingPrice;
    private LocalDateTime startDate;
    private Double sellingPrice;
    private LocalDateTime endDate;
    private Double profit;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @PrePersist
    private void onCreate(){
        startDate = LocalDateTime.now();
    }

    public Double getProfit() {
        return amount * sellingPrice - amount * buyingPrice;
    }
}
