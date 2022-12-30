package com.simon.portfoliotracker.transaction;

import com.simon.portfoliotracker.ownedToken.OwnedToken;
import com.simon.portfoliotracker.token.Token;
import com.simon.portfoliotracker.token.TokenService;
import com.simon.portfoliotracker.wallet.Wallet;
import com.simon.portfoliotracker.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final TokenService tokenService;
    private final ModelMapper mapper;
    private final ApplicationEventPublisher publisher;

    @PostConstruct
    private void postConstruct(){
        Converter<Token, String> fullTokenName = mappingContext ->
                generateFullTokenName(mappingContext.getSource().getName(), mappingContext.getSource().getSymbol());

        Converter<LocalDateTime, String> date = mappingContext -> formatDate(mappingContext.getSource());
        mapper.typeMap(Token.class, String.class).setConverter(fullTokenName);
        mapper.typeMap(LocalDateTime.class, String.class).setConverter(date);
    }

    String generateFullTokenName(String name, String symbol){
        if(!name.equals(symbol)){
            return String.format("%s (%s)", name, symbol);
        }
        return name;
    }

    private String formatDate(LocalDateTime date){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm");
        return date.format(format);
    }

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
        return transactionRepository.findAll().stream()
                .map(transaction -> mapper.map(transaction, TransactionRead.class))
                .collect(Collectors.toList());
    }




}
