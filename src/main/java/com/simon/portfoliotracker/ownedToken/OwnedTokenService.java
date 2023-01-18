package com.simon.portfoliotracker.ownedToken;

import com.simon.portfoliotracker.token.Token;
import com.simon.portfoliotracker.wallet.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OwnedTokenService {

    private final OwnedTokenRepository repository;

    public Optional<OwnedToken> findOwnedTokenByWalletAndToken(Wallet wallet, Token token){
        return repository.findOwnedTokenByWalletAndToken(wallet, token);
    }
    public void updateOwnedTokenAfterTransactionDeletion(Wallet wallet, Token token, Double amount) {
        OwnedToken ownedToken = findOwnedTokenByWalletAndToken(wallet, token).get();
        ownedToken.setAmount(ownedToken.getAmount() - amount);
        if (ownedToken.getAmount() == 0){
            repository.delete(ownedToken);
            return;
        }
        decrementTransactionCounter(ownedToken);
    }
    private void mergeExistingOwnedToken(OwnedToken ownedToken, Double amount, Double buyingPrice){
        ownedToken.setAmount(ownedToken.getAmount() + amount);
        incrementTransactionCounter(ownedToken);
        ownedToken.setPrice((ownedToken.getPrice() + buyingPrice) / ownedToken.getTransactionCount());
    }
    private void incrementTransactionCounter(OwnedToken ownedToken){
        ownedToken.setTransactionCount(ownedToken.getTransactionCount() + 1);
    }
    private void decrementTransactionCounter(OwnedToken ownedToken){
        ownedToken.setTransactionCount(ownedToken.getTransactionCount() - 1);
    }

    public void updateOwnedTokenAfterTransactionAdding(Wallet wallet, Token token, Double amount, Double buyingPrice){
        Optional<OwnedToken> ownedToken = findOwnedTokenByWalletAndToken(wallet, token);
        if(ownedToken.isPresent()){
            mergeExistingOwnedToken(ownedToken.get(), amount, buyingPrice);
        } else {
            wallet.getTokens().add(new OwnedToken(wallet, token, amount, buyingPrice));
        }
    }
}
