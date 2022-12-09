package com.simon.portfoliotracker;

import com.simon.portfoliotracker.auth.user.AppUserService;
import com.simon.portfoliotracker.auth.user.ApplicationUser;
import com.simon.portfoliotracker.auth.user.UserRepository;
import com.simon.portfoliotracker.auth.user.UserRole;
import com.simon.portfoliotracker.ownedToken.OwnedTokenRepository;
import com.simon.portfoliotracker.token.TokenService;
import com.simon.portfoliotracker.transaction.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PortfolioTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortfolioTrackerApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository,
                                      TransactionRepository transactionRepository,
                                      OwnedTokenRepository ownedTokenRepository,
                                      TokenService tokenService,
                                      AppUserService userService){
        return args -> {
            userRepository.save(new ApplicationUser("Szymon", "$2a$12$WXxGhMPgxFSerYq/bTnCd.ywLvW226kP6bhxDeLrApLTMwgHW7Czq", UserRole.ADMIN));
            userRepository.save(new ApplicationUser("Iza", "$2a$12$WXxGhMPgxFSerYq/bTnCd.ywLvW226kP6bhxDeLrApLTMwgHW7Czq", UserRole.USER));

            tokenService.fillDatabase();
//            tokenService.getRepoTokens().stream().forEach(System.out::println);
//            System.out.println(tokenService.findByName("Bitcoin"));
//            System.out.println(tokenService.getCurrentPrice(tokenService.findByName("Bitcoin")));
            System.out.println(userService.getUserDto("Szymon"));
        };
    }
}
