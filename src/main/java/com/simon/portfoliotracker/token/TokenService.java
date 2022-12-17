package com.simon.portfoliotracker.token;

import com.simon.portfoliotracker.token.api.MultiTokenInfo;
import com.simon.portfoliotracker.token.api.SingleTokenInfo;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TokenService {
    public static final String BASE_URL = "api.coincap.io/v2";
    private TokenRepository tokenRepository;
    private ModelMapper modelMapper;
    private final WebClient webClient;

    public TokenService(TokenRepository tokenRepository, ModelMapper modelMapper, WebClient.Builder builder) {
        this.tokenRepository = tokenRepository;
        this.modelMapper = modelMapper;
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

    public Token findByName(String name){
        return tokenRepository.findByName(name).orElseThrow(() -> new NoSuchElementException("Token with name " + name + " not found"));
    }

    public List<Token> getRepoTokens(){
        return tokenRepository.findAll();
    }

    public TokenDto mapToDto(Token token){
        TokenDto tokenDto = modelMapper.map(token, TokenDto.class);
        tokenDto.setPrice(getCurrentPrice(token));
        return tokenDto;
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
                .uri("/assets/" + token.getUrlName())
                .retrieve()
                .bodyToMono(SingleTokenInfo.class)
                .block()
                .getData().getCurrentPrice();
    }

    public List<String> getTokensByName() {
        return getRepoTokens().stream().map(Token::getName).collect(Collectors.toList());
    }

    public List<TokenDto> fetchCryptoPrices() {
        return getRepoTokens().stream()
                .parallel()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
