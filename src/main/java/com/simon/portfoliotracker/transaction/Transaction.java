package com.simon.portfoliotracker.transaction;

import com.simon.portfoliotracker.token.Token;
import com.simon.portfoliotracker.wallet.Wallet;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @PrePersist
    private void onCreate(){
        date = LocalDateTime.now();
    }


    public static Builder Builder(){
        return new Builder();
    }

    public static final class Builder{
        private Wallet wallet;
        private Token token;
        private Double amount;
        private Double buyingPrice;
        private TransactionType transactionType;
        private LocalDateTime transactionDate;

        Builder wallet(Wallet wallet){
            this.wallet = wallet;
            return this;
        }
        Builder token(Token token){
            this.token = token;
            return this;
        }
        Builder amount(Double amount) {
            this.amount = amount;
            return this;
        }
        Builder buyingPrice(Double buyingPrice){
            this.buyingPrice = buyingPrice;
            return this;
        }
        Builder transactionType(TransactionType transactionType){
            this.transactionType = transactionType;
            return this;
        }

        Builder transactionDate(LocalDateTime date){
            this.transactionDate = date;
            return this;
        }

        public Transaction build(){
            Transaction transaction = new Transaction();
            transaction.wallet = this.wallet;
            transaction.token = this.token;
            transaction.amount = this.amount;
            transaction.buyingPrice = this.buyingPrice;
            transaction.transactionType = this.transactionType;
            transaction.date = this.transactionDate;
            return transaction;
        }
    }


}
