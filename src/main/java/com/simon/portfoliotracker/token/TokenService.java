package com.simon.portfoliotracker.token;

import com.simon.portfoliotracker.token.api.MultiTokenInfo;
import com.simon.portfoliotracker.token.api.SingleTokenInfo;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TokenService {
    public static final String BASE_URL = "api.coincap.io/v2";
    private TokenRepository tokenRepository;
    private final WebClient webClient;

    public TokenService(TokenRepository tokenRepository, WebClient.Builder builder) {
        this.tokenRepository = tokenRepository;
        webClient = builder.baseUrl(BASE_URL)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().followRedirect(true)
                ))
                .build();
    }

    public List<Token> getTokens(){
        return webClient.get()
                .uri("/assets")
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(MultiTokenInfo.class)
                .block()
                .getData();
    }

    public Token findById(Long id){
        return tokenRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Token with id "+ id +" not found"));
    }


    public void fillDatabase(){
            tokenRepository.saveAll(getTokens().stream()
                    .filter(token -> !(tokenRepository.findAll()
                            .stream().map(Token::getName).collect(Collectors.toList())
                            .contains(token.getName())))
                    .collect(Collectors.toList()));
    }

    public Double getCurrentPrice(Token token){
        return webClient.get()
                .uri("/assets/" + token.getName().toLowerCase())
                .retrieve()
                .bodyToMono(SingleTokenInfo.class)
                .block()
                .getData().getCurrentPrice();
    }
}
