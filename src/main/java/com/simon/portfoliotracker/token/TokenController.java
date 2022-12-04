package com.simon.portfoliotracker.token;

import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController

public class TokenController {
    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/tokens")
    public ResponseEntity<List<TokenDto>> getTokens(){
        List<TokenDto> tokenDtos = tokenService.getRepoTokens().stream()
                .map(tokenService::mapToDto).collect(Collectors.toList());
        return ResponseEntity.ok(tokenDtos);
    }

    @GetMapping("/tokens/{name}")
    public ResponseEntity<TokenDto> getToken(@PathVariable String name){
        return ResponseEntity.ok(tokenService.mapToDto(tokenService.findByName(name)));
    }


}
