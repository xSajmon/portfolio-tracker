package com.simon.portfoliotracker.token;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenService {
    private TokenRepository tokenRepository;
    private final WebClient webClient;

    public TokenService(TokenRepository tokenRepository, WebClient.Builder builder) {
        this.tokenRepository = tokenRepository;
        webClient = builder.baseUrl("api.coincap.io/v2")
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
                .bodyToMono(ApiTokenInfo.class)
                .block()
                .getData();
    }


    public void fillDatabase(){
            tokenRepository.saveAll(getTokens().stream()
                    .filter(token -> !(tokenRepository.findAll()
                            .stream().map(Token::getName).collect(Collectors.toList())
                            .contains(token.getName())))
                    .collect(Collectors.toList()));
    }
}
