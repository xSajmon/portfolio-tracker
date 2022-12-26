package com.simon.portfoliotracker.wallet;

import com.simon.portfoliotracker.ownedToken.OwnedToken;
import com.simon.portfoliotracker.transaction.Transaction;
import com.sun.xml.bind.v2.TODO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public Wallet findWalletById(Long id){
       return walletRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Wallet with id " + id + "not found."));
    }

    public Double getWalletBalance(Long id){
        return findWalletById(id).getBalance();
    }

    public void updateWallet(Transaction transaction){
        Wallet wallet = findWalletById(transaction.getWallet().getId());
        switch (transaction.getTransactionType()){
            case BUY: {
                wallet.setBalance(wallet.getBalance() - transaction.getAmount());
                wallet.getTokens().add(new OwnedToken(wallet, transaction.getToken(), transaction.getAmount(), transaction.getBuyingPrice()));
            }
            case SELL: //TODO;
        }
        walletRepository.save(wallet);
    }
}
