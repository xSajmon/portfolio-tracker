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
        Converter<Token, String> fullTokenName = new Converter<Token, String>() {
            @Override
            public String convert(MappingContext<Token, String> mappingContext) {
                return generateFullTokenName(mappingContext.getSource().getName(), mappingContext.getSource().getSymbol());
            }
        };
        Converter<LocalDateTime, String> date = new Converter<LocalDateTime, String>() {
            @Override
            public String convert(MappingContext<LocalDateTime, String> mappingContext) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm");
                return mappingContext.getSource().format(format);
            }
        };
        mapper.addConverter(fullTokenName);
        mapper.addConverter(date);
    }

    public Transaction buyTransaction(TransactionWrite transactionDto){
        Wallet wallet = walletService.findWalletById(transactionDto.getWalletId());
        Token token = tokenService.findByName(transactionDto.getToken());
        Double amount = transactionDto.getAmount();
        Transaction transaction = new Transaction(wallet, token, amount, TransactionType.BUY, tokenService.getCurrentPrice(token));
;
        wallet.setBalance(wallet.getBalance()- amount);
        wallet.getTokens().add(new OwnedToken(wallet, token, amount, tokenService.getCurrentPrice(token)));
        Transaction save = transactionRepository.save(transaction);
        publisher.publishEvent(new TransactionAddedEvent(getTransactions()));
        return save;
    }

    public List<TransactionRead> getTransactions(){
        return transactionRepository.findAll().stream()
                .map(transaction -> mapper.map(transaction, TransactionRead.class))
                .collect(Collectors.toList());
    }

    private String generateFullTokenName(String name, String symbol){
        if(!name.equals(symbol)){
            return String.format("%s (%s)", name, symbol);
        }
        return name;
    }


}
