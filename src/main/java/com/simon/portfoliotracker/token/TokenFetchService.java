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

import static org.springframework.web.reactive.function.client.WebClient.builder;

@Service
public class TokenFetchService {
    public static final String BASE_URL = "api.coincap.io/v2";
    private final WebClient webClient;

    TokenFetchService(){
        webClient = builder().baseUrl(BASE_URL)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().followRedirect(true)
                ))
                .build();
    }
    List<Token> getTokens(){
        return webClient.get()
                .uri("/assets")
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(MultiTokenInfo.class)
                .block()
                .getData();
    }
    public Double getCurrentTokenPrice(Token token){
        return webClient.get()
                .uri("/assets/" + token.getUrlName())
                .retrieve()
                .bodyToMono(SingleTokenInfo.class)
                .block()
                .getData().getCurrentPrice();
    }
}
