package com.simon.portfoliotracker.transaction;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonPropertyOrder({"id"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionRead extends TransactionWrite {


    private Long id;
    private String type;
    private String startDate;
    private String endDate;
    @JsonProperty("P&L")
    private Double profitAndLoss;


}
