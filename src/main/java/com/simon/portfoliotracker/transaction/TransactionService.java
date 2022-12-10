package com.simon.portfoliotracker.transaction;

import com.simon.portfoliotracker.ownedToken.OwnedToken;
import com.simon.portfoliotracker.token.Token;
import com.simon.portfoliotracker.token.TokenService;
import com.simon.portfoliotracker.wallet.Wallet;
import com.simon.portfoliotracker.wallet.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionService {

    private TransactionRepository transactionRepository;
    private WalletService walletService;
    private TokenService tokenService;

    public Transaction buyTransaction(TransactionDTO transactionDto){
        Wallet wallet = walletService.findWalletById(transactionDto.getWalletId());
        Token token = tokenService.findByName(transactionDto.getToken());
        Double amount = transactionDto.getAmount();
        Transaction transaction = new Transaction(wallet, token, amount, TransactionType.BUY);
        wallet.setBalance(wallet.getBalance()- amount);
        wallet.getTokens().add(new OwnedToken(wallet, token, amount, tokenService.getCurrentPrice(token)));
        return transactionRepository.save(transaction);
    }
}
