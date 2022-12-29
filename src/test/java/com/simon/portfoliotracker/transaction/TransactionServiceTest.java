package com.simon.portfoliotracker.transaction;

import com.simon.portfoliotracker.token.Token;
import com.simon.portfoliotracker.token.TokenService;
import com.simon.portfoliotracker.wallet.Wallet;
import com.simon.portfoliotracker.wallet.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private WalletService walletService;
    @Mock
    private TokenService tokenService;
    @Mock
    private ModelMapper mapper;
    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private TransactionService transactionService;


    @Test
    public void Should_FormatTokenName_When_TokenNameAndTokenSymbolAreDifferent(){
        // given
        String name = "Bitcoin";
        String symbol = "BTC";

        // when
        String actualResult = transactionService.generateFullTokenName(name, symbol);
        String expectedResult = "Bitcoin (BTC)";

        // then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void Should_ReturnTokenName_When_TokenNameAndTokenSymbolAreEqual(){
        //given
        String name = "Bitcoin";
        String symbol = "Bitcoin";

        //when
        String actualResult = transactionService.generateFullTokenName(name, symbol);
        String expectedResult = "Bitcoin";

        //then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void Should_ThrowNoSuchElementException_When_WalletWithGivenIdDoesNotExists(){
        //given
        TransactionWrite transactionWrite = new TransactionWrite();
        transactionWrite.setWalletId(1L);
        transactionWrite.setToken("Bitcoin");
        transactionWrite.setAmount(100d);
        given(walletService.findWalletById(anyLong())).willThrow(NoSuchElementException.class);

        //when
        Exception exception = assertThrows(NoSuchElementException.class, () ->
            transactionService.buyTransaction(transactionWrite)
        );

        //then
        assertThat(exception).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void Should_ThrowNoSuchElementException_When_TokenWithGivenNameDoesNotExists(){
        //given
        TransactionWrite transactionWrite = new TransactionWrite();
        transactionWrite.setWalletId(1L);
        transactionWrite.setToken("Bitcoin");
        transactionWrite.setAmount(100d);
        Wallet wallet = new Wallet();
        given(walletService.findWalletById(anyLong())).willReturn(wallet);
        when(tokenService.findByName(anyString())).thenThrow(NoSuchElementException.class);

        //when
        Exception exception = assertThrows(NoSuchElementException.class, () ->
                transactionService.buyTransaction(transactionWrite)
        );
        //then
        assertThat(exception).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void Should_CreateSaveAndReturnNewTransactionAndPublishEvent_When_WalletAndTokenAreValid(){
        //given
        TransactionWrite transactionWrite = new TransactionWrite();
        transactionWrite.setWalletId(1L);
        transactionWrite.setToken("Bitcoin");
        transactionWrite.setAmount(100d);
        Wallet wallet = new Wallet();
        Token token = new Token("Bitcoin", "BTC");

        given(walletService.findWalletById(anyLong())).willReturn(wallet);
        given(tokenService.findByName(anyString())).willReturn(token);
        given(tokenService.getCurrentPrice(any(Token.class))).willReturn(20000d);
        given(transactionRepository.save(any(Transaction.class))).willAnswer(i -> i.getArguments()[0]);

        //when
        Transaction actualTransaction = transactionService.buyTransaction(transactionWrite);
        Transaction expectedTransaction = new Transaction(wallet, token, 100d, TransactionType.BUY, 20000d);

        //then
        assertEquals(expectedTransaction, actualTransaction);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(publisher).publishEvent(any(TransactionAddedEvent.class));
    }


}