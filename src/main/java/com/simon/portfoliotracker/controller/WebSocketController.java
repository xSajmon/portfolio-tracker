package com.simon.portfoliotracker.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {


    @MessageMapping("/transactions")
    @SendTo("/topic/transactions")
    public String broadCastTransactions(@Payload String message){
        return message;
    }
}
