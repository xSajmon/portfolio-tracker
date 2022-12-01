package com.simon.portfoliotracker.ownedToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnedTokenRepository extends JpaRepository<OwnedToken, Long> {
}
