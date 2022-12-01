package com.simon.portfoliotracker;

import com.simon.portfoliotracker.auth.ApplicationUser;
import com.simon.portfoliotracker.auth.UserRepository;
import com.simon.portfoliotracker.auth.UserRole;
import com.simon.portfoliotracker.ownedToken.OwnedToken;
import com.simon.portfoliotracker.ownedToken.OwnedTokenRepository;
import com.simon.portfoliotracker.token.Token;
import com.simon.portfoliotracker.transaction.Transaction;
import com.simon.portfoliotracker.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class PortfolioTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortfolioTrackerApplication.class, args);
    }


    @Bean
    public CommandLineRunner loadData(UserRepository userRepository,
                                      TransactionRepository transactionRepository,
                                      OwnedTokenRepository ownedTokenRepository){
        return args -> {
            userRepository.save(new ApplicationUser("Szymon", "$2a$12$WXxGhMPgxFSerYq/bTnCd.ywLvW226kP6bhxDeLrApLTMwgHW7Czq", UserRole.ADMIN));
            userRepository.save(new ApplicationUser("Iza", "$2a$12$WXxGhMPgxFSerYq/bTnCd.ywLvW226kP6bhxDeLrApLTMwgHW7Czq", UserRole.USER));
            ApplicationUser user = userRepository.findApplicationUserByUsername("Szymon").get();
            transactionRepository.save(new Transaction(user.getWallet(), null, 200d));
            Transaction transaction = new Transaction(user.getWallet(), new Token("Bitcoin", "BTC", 20000d), 100d);
            transactionRepository.save(transaction);
            ownedTokenRepository.save(new OwnedToken(transaction.getWallet(), transaction.getToken(), transaction.getAmount()));

        };
    }
}
