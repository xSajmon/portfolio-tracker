package com.simon.portfoliotracker.token;

import java.util.List;

public interface Observer {
    void update(List<TokenDto> tokens);
}
