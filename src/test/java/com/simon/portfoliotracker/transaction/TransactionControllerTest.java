package com.simon.portfoliotracker.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;



@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerTest {

   private static final String ENDPOINT = "/transactions";
   private static final String TOKEN_VALIDATION_ERROR = "Please choose a token.";
   private static final String AMOUNT_VALIDATION_ERROR = "Incorrect amount.";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TransactionService transactionService;

    private TransactionWrite transaction;

    @BeforeEach
    public void init(){
        transaction = new TransactionWrite();
        transaction.setToken("Bitcoin");
        transaction.setWalletId(1L);
        transaction.setAmount(200d);
    }

    @Test
    public void Should_Return201Created_When_PostRequestCalledAndBodyIsValid() throws Exception {

        given(transactionService.buyTransaction(any(TransactionWrite.class))).willReturn(any(Transaction.class));

        mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(transaction)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void Should_Return400BadRequestAndErrorResult_When_PostRequestCalledAndTokenIsBlank() throws Exception {

        transaction.setToken("");

        mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(transaction)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.token", is(TOKEN_VALIDATION_ERROR)));


    }

    @Test
    public void Should_Return400BadRequestAndErrorResult_When_PostRequestCalledAndAmountIsIncorrect() throws Exception {

        transaction.setAmount(0d);

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(transaction)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.amount", is(AMOUNT_VALIDATION_ERROR)));
    }


    @Test
    public void Should_Return200Ok_WhenGetRequestCalled() throws Exception {
        given(transactionService.getTransactions()).willReturn(new ArrayList<>());

        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk());
    }

}