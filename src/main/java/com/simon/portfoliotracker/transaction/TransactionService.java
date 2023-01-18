package com.simon.portfoliotracker.transaction;

import com.simon.portfoliotracker.token.TokenService;
import com.simon.portfoliotracker.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final TokenService tokenService;
    private final TransactionMapper mapper;
    private final ApplicationEventPublisher publisher;

    public List<TransactionRead> getTransactions(){
        return mapper.mapToDtos(transactionRepository.findAll());
    }

    public Transaction getTransactionById(Long id){
        return transactionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Transaction with id " + id + " not found"));
    }

    public Transaction buyTransaction(TransactionWrite transactionDto){

        Transaction transaction = Transaction.builder()
                .wallet(walletService.findWalletById(transactionDto.getWalletId()))
                .token(tokenService.findByName(transactionDto.getToken()))
                .amount(transactionDto.getAmount())
                .buyingPrice(tokenService.getCurrentPrice(tokenService.findByName(transactionDto.getToken())))
                .transactionType(TransactionType.ACTIVE)
                .build();

        Transaction save = transactionRepository.save(transaction);
        publisher.publishEvent(new TransactionAddedEvent(save, getTransactions()));
        return save;
    }

    public void sellTransaction(Long id){
        Transaction transaction = getTransactionById(id);
        transaction.setTransactionType(TransactionType.DELETED);
        transactionRepository.delete(transaction);
        publisher.publishEvent(new TransactionDeletedEvent(transaction, getTransactions()));
    }






}
