package com.simon.portfoliotracker.transaction;

import com.simon.portfoliotracker.token.TokenService;
import com.simon.portfoliotracker.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final TokenService tokenService;
    private final TransactionMapper mapper;
    private final ApplicationEventPublisher publisher;



    public Transaction buyTransaction(TransactionWrite transactionDto){

        Transaction transaction = Transaction.Builder()
                .wallet(walletService.findWalletById(transactionDto.getWalletId()))
                .token(tokenService.findByName(transactionDto.getToken()))
                .amount(transactionDto.getAmount())
                .buyingPrice(tokenService.getCurrentPrice(tokenService.findByName(transactionDto.getToken())))
                .transactionType(TransactionType.BUY)
                .build();

        Transaction save = transactionRepository.save(transaction);
        publisher.publishEvent(new TransactionAddedEvent(save, getTransactions()));
        return save;
    }

    public List<TransactionRead> getTransactions(){
        return mapper.mapToDtos(transactionRepository.findAll());
    }




}
