package com.simon.portfoliotracker.ownedToken;

import com.simon.portfoliotracker.token.Token;
import com.simon.portfoliotracker.wallet.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnedTokenServiceTest {

    @Mock
    private OwnedTokenRepository repository;

    @InjectMocks
    private OwnedTokenService service;

    @Test
    public void Should_ReturnEmptyOptional_WhenOwnedTokenWithGivenWalletAndTokenNotFound(){
        //given
        given(repository.findOwnedTokenByWalletAndToken(any(Wallet.class), any(Token.class))).willReturn(Optional.empty());

        //when
        Optional<OwnedToken> ownedToken = service.findOwnedTokenByWalletAndToken(new Wallet(), new Token());

        //then
        assertEquals(ownedToken, Optional.empty());

    }
    @Test
    public void Should_ResetOwnedTokenAmountTo0AndDeleteToken_When_OwnedTokenAmountIs200AndTransactionAmountWas200(){
        //given
        OwnedToken ownedToken = new OwnedToken();
        ownedToken.setAmount(200d);
        given(repository.findOwnedTokenByWalletAndToken(any(Wallet.class), any(Token.class))).willReturn(Optional.of(ownedToken));

        //when
        service.updateOwnedTokenAfterTransactionDeletion(new Wallet(), new Token(), 200d);

        //then
        assertEquals(ownedToken.getAmount(), 0);
        verify(repository, times(1)).delete(ownedToken);
    }

    @Test
    public void Should_UpdateOwnedTokenAmountTo100AndDecrementCounter_WhenOwnedTokenAmountIs200AndTransactionAmountWas100(){
        //given
        OwnedToken ownedToken = new OwnedToken();
        ownedToken.setAmount(200d);
        ownedToken.setTransactionCount(2L);
        given(repository.findOwnedTokenByWalletAndToken(any(Wallet.class), any(Token.class))).willReturn(Optional.of(ownedToken));

        //when
        service.updateOwnedTokenAfterTransactionDeletion(new Wallet(), new Token(), 100d);

        //then

        assertEquals(ownedToken.getAmount(), 100);
        assertEquals(ownedToken.getTransactionCount(), 1);
        verify(repository, never()).delete(ownedToken);
    }

    @Test
    public void Should_AddNewOwnedTokenToWallet_When_NewTransactionAppearedAndOwnedTokenNotFound(){
        //given
        Wallet wallet = new Wallet();
        wallet.setTokens(new ArrayList<>());
        Token token = new Token();
        given(repository.findOwnedTokenByWalletAndToken(any(Wallet.class), any(Token.class))).willReturn(Optional.empty());

        //when
        service.updateOwnedTokenAfterTransactionAdding(wallet, token, 200d ,200d);

        //then
        assertThat(wallet.getTokens()).hasSize(1);
        assertThat(wallet.getTokens()).contains(new OwnedToken(wallet, token, 200d, 200d));
    }

    @Test
    public void Should_MergeTokensIncreaseAmountTo400SetCounterTo2AndUpdatePriceTo150_When_NewTransactionAppeared(){
        //given
        OwnedToken ownedToken = new OwnedToken();
        ownedToken.setAmount(200d);
        ownedToken.setPrice(200d);
        ownedToken.setTransactionCount(1L);
        given(repository.findOwnedTokenByWalletAndToken(any(Wallet.class), any(Token.class))).willReturn(Optional.of(ownedToken));


        //when
        service.updateOwnedTokenAfterTransactionAdding(new Wallet(), new Token(), 200d, 100d);

        //then
        assertEquals(ownedToken.getAmount(), 400);
        assertEquals(ownedToken.getTransactionCount(), 2);
        assertEquals(ownedToken.getPrice(), 150);
    }

}