package com.simon.portfoliotracker.ownedToken;

import com.simon.portfoliotracker.token.Token;
import com.simon.portfoliotracker.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnedTokenRepository extends JpaRepository<OwnedToken, Long> {
    Optional<OwnedToken> findOwnedTokenByWalletAndToken(Wallet wallet, Token token);
}
