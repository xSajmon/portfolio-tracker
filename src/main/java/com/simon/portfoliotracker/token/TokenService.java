package com.simon.portfoliotracker.token;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    private final ModelMapper modelMapper;
    private final TokenFetchService tokenFetchService;

    public List<Token> getRepoTokens(){
        return tokenRepository.findAll();
    }

    public List<String> getTokensByName() {
        return getRepoTokens().stream().map(Token::getName).collect(Collectors.toList());
    }

    public Token findById(Long id){
        return tokenRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Token with id "+ id +" not found"));
    }

    public Token findByName(String name){
        return tokenRepository.findByName(name).orElseThrow(() -> new NoSuchElementException("Token with name " + name + " not found"));
    }

    public TokenDto mapToDto(Token token){
        TokenDto tokenDto = modelMapper.map(token, TokenDto.class);
        tokenDto.setPrice(tokenFetchService.getCurrentTokenPrice(token));
        return tokenDto;
    }
    public Double getCurrentPrice(Token token) {
        return tokenFetchService.getCurrentTokenPrice(token);
    }
    public void fillDatabase(){
        tokenRepository.saveAll(tokenFetchService.getTokens().stream()
                .filter(token -> !(tokenRepository.findAll()
                        .stream().map(Token::getName).collect(Collectors.toList())
                        .contains(token.getName())))
                .collect(Collectors.toList()));
    }
}
