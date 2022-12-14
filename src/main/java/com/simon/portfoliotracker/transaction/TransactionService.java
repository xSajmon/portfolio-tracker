package com.simon.portfoliotracker.transaction;

import com.simon.portfoliotracker.ownedToken.OwnedToken;
import com.simon.portfoliotracker.token.Token;
import com.simon.portfoliotracker.token.TokenService;
import com.simon.portfoliotracker.wallet.Wallet;
import com.simon.portfoliotracker.wallet.WalletService;
import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService {

    private TransactionRepository transactionRepository;
    private WalletService walletService;
    private TokenService tokenService;
    private ModelMapper mapper;

    @PostConstruct
    private void postConstruct(){
        Converter<Token, String> fullTokenName = new Converter<Token, String>() {
            @Override
            public String convert(MappingContext<Token, String> mappingContext) {
                return generateFullTokenName(mappingContext.getSource().getName(), mappingContext.getSource().getSymbol());
            }
        };
        mapper.addConverter(fullTokenName);
    }

    public Transaction buyTransaction(TransactionWrite transactionDto){
        Wallet wallet = walletService.findWalletById(transactionDto.getWalletId());
        Token token = tokenService.findByName(transactionDto.getToken());
        Double amount = transactionDto.getAmount();
        Transaction transaction = new Transaction(wallet, token, amount, TransactionType.BUY);
        wallet.setBalance(wallet.getBalance()- amount);
        wallet.getTokens().add(new OwnedToken(wallet, token, amount, tokenService.getCurrentPrice(token)));
        return transactionRepository.save(transaction);
    }

    public List<TransactionRead> getTransactions(){
        return transactionRepository.findAll().stream()
                .map(transaction -> mapper.map(transaction, TransactionRead.class))
                .collect(Collectors.toList());
    }

    private String generateFullTokenName(String name, String symbol){
        if(!name.equals(symbol)){
            return String.format("%s(%s)", name, symbol);
        }
        return name;
    }

}
