package com.simon.portfoliotracker.wallet;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallets")
@CrossOrigin(origins = "http://localhost:4200")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/{id}/balance")
    public Double getWalletBalance(@PathVariable Long id){
        return walletService.getWalletBalance(id);
    }
}
