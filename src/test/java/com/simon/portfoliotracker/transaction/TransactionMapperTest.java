package com.simon.portfoliotracker.transaction;

import com.simon.portfoliotracker.token.Token;
import com.simon.portfoliotracker.wallet.Wallet;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class TransactionMapperTest {
    private TransactionMapper transactionMapper;


    @Test
    public void Should_ProperlyMapTransactionToDto(){
        //given
        transactionMapper = new TransactionMapper(new ModelMapper());
        Transaction transaction = buildTestTransaction();

        //when
        TransactionRead transactionRead = transactionMapper.mapToDto(transaction);

        //then
        assertEquals(transactionRead.getType(), "ACTIVE");
        assertEquals(transactionRead.getBuyingPrice(), 1000);
        assertEquals(transactionRead.getAmount(), 200);
        assertEquals(transactionRead.getToken(), "Bitcoin(BTC)");
        assertEquals(transactionRead.getStartDate(), "14-10-99 00:00");


    }
    private Transaction buildTestTransaction(){
        return Transaction.builder()
                .wallet(new Wallet())
                .buyingPrice(1000d)
                .amount(200d)
                .transactionType(TransactionType.ACTIVE)
                .token(new Token("Bitcoin", "BTC"))
                .startDate(LocalDateTime.of(1999, Month.OCTOBER, 14, 0, 0))
                .endDate(null)
                .build();
    }
}