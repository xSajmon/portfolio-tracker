package com.simon.portfoliotracker.transaction;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonPropertyOrder({"id"})
public class TransactionRead extends TransactionWrite {


    private Long id;
    private String type;
    private String date;
    private Double buyingPrice;
}
