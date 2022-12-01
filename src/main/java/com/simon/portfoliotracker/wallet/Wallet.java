package com.simon.portfoliotracker.wallet;

import com.simon.portfoliotracker.auth.ApplicationUser;
import com.simon.portfoliotracker.ownedToken.OwnedToken;
import com.simon.portfoliotracker.token.Token;
import com.simon.portfoliotracker.transaction.Transaction;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class Wallet {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double balance;

    @OneToOne(mappedBy = "wallet")
    private ApplicationUser user;

    @OneToMany(mappedBy = "wallet")
    private Set<OwnedToken> tokens;

    @OneToMany(mappedBy = "wallet")
    private Set<Transaction> transactions;

    public Wallet() {
        this.balance = 0d;
    }
}
