package com.simon.portfoliotracker.wallet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.simon.portfoliotracker.auth.user.ApplicationUser;
import com.simon.portfoliotracker.ownedToken.OwnedToken;
import com.simon.portfoliotracker.transaction.Transaction;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double balance;

    @OneToOne(mappedBy = "wallet")
    @JsonIgnore
    private ApplicationUser user;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OwnedToken> tokens;

    @OneToMany(mappedBy = "wallet")
    @JsonIgnore
    private List<Transaction> transactions;

    public Wallet() {
        this.balance = 500d;
    }
}
