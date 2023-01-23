package com.simon.portfoliotracker.token;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class TokenPricePublisher {

    private List<Token> tokens = new ArrayList<>();
    private final List<Observer> observers = new ArrayList<>();
    private final ModelMapper modelMapper;
    private final TokenFetchService tokenFetchService;


    @Scheduled(fixedRate = 1000)
    private void findDifferences(){
        Map<String, Double> oldList = tokens.stream().collect(Collectors.toMap(Token::getName, Token::getCurrentPrice));
        this.tokens = tokenFetchService.getTokens();
        List<TokenDto> finalList = tokens.stream()
                .filter(token -> !token.getCurrentPrice().equals(oldList.get(token.getName())))
                .map(token -> modelMapper.map(token, TokenDto.class))
                .collect(Collectors.toList());
        if(!finalList.isEmpty()){
            notifyObservers(finalList);
        }
    }

    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers(List<TokenDto> tokens) {
        observers.forEach(observer -> observer.update(tokens));
    }
}
