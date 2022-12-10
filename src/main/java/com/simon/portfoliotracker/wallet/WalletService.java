package com.simon.portfoliotracker.wallet;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public Wallet findWalletById(Long id){
       return walletRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Wallet with id " + id + "not found."));
    }
}
