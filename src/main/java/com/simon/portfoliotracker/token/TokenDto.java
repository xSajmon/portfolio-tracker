package com.simon.portfoliotracker.token;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TokenDto {
    private String name;
    private String symbol;
    private Double price;
}
