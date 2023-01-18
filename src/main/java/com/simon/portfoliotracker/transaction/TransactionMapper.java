package com.simon.portfoliotracker.transaction;

import com.simon.portfoliotracker.base.AbstractMapper;
import com.simon.portfoliotracker.token.Token;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TransactionMapper extends AbstractMapper<Transaction, TransactionRead> {
    private final ModelMapper mapper;

    public TransactionMapper(ModelMapper mapper) {
        this.mapper = mapper;
        TypeMap<Transaction, TransactionRead> typeMap = mapper.createTypeMap(Transaction.class, TransactionRead.class);
        typeMap.addMappings(typeMapper -> typeMapper.using(createTokenNameConverter()).map(Transaction::getToken, TransactionRead::setToken));
        typeMap.addMappings(typeMapper -> typeMapper.using(createDateConverter()).map(Transaction::getStartDate, TransactionRead::setStartDate));
    }
    @Override
    public TransactionRead mapToDto(Transaction entity) {
        return mapper.map(entity, TransactionRead.class);
    }

    @Override
    public Transaction mapToEntity(TransactionRead dto) {
        return mapper.map(dto, Transaction.class);
    }

    private Converter<Token, String> createTokenNameConverter(){
        return ctx -> generateFullTokenName(ctx.getSource().getName(), ctx.getSource().getSymbol());
    }
    private String generateFullTokenName(String name, String symbol){
        if(!name.equals(symbol)){
            return String.format("%s(%s)", name, symbol);
        }
        return name;
    }

    private Converter<LocalDateTime, String> createDateConverter(){
        return ctx -> formatDate(ctx.getSource());
    }
    private String formatDate(LocalDateTime date){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm");
        return date.format(format);
    }

}
