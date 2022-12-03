package com.simon.portfoliotracker.token;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

class ApiTokenInfo {
    private List<Token> data;
    private long timestamp;

   public List<Token> getData() {
        return data;
    }
}
