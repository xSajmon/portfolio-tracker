package com.simon.portfoliotracker.token.api;


class BaseTokenInfo<T> {

    private T data;
    private Long timestamp;

    public T getData() {
        return data;
    }
}
