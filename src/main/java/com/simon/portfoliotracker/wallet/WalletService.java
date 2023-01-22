package com.simon.portfoliotracker.wallet;

import com.simon.portfoliotracker.ownedToken.OwnedToken;
import com.simon.portfoliotracker.ownedToken.OwnedTokenService;
import com.simon.portfoliotracker.token.Token;
import com.simon.portfoliotracker.transaction.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final OwnedTokenService ownedTokenService;

    public Wallet findWalletById(Long id){
       return walletRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Wallet with id " + id + "not found."));
    }
    public Double getWalletBalance(Long id){
        return findWalletById(id).getBalance();
    }

    public void updateWallet(Transaction transaction){
        Wallet wallet = findWalletById(transaction.getWallet().getId());
        switch (transaction.getTransactionType()){
            case ACTIVE: {
                wallet.setBalance(wallet.getBalance() - transaction.getAmount());
                ownedTokenService.updateOwnedTokenAfterTransactionAdding(
                        wallet, transaction.getToken(), transaction.getAmount(), transaction.getBuyingPrice());
                break;
            }
            case DELETED: {
                wallet.setBalance(wallet.getBalance() + transaction.getAmount());
                ownedTokenService.updateOwnedTokenAfterTransactionDeletion(wallet, transaction.getToken(), transaction.getAmount());
                break;
            }
            case COMPLETED: {
                wallet.setBalance(wallet.getBalance() + transaction.getAmount() + transaction.getProfit());
                ownedTokenService.updateOwnedTokenAfterTransactionDeletion(wallet, transaction.getToken(), transaction.getAmount());
                break;
            }
        }
        walletRepository.save(wallet);
    }
}
