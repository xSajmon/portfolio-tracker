package com.simon.portfoliotracker.transaction;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Object> addTransaction(@Valid @RequestBody TransactionDTO transactionDTO,
                                                  BindingResult result){
        if(result.hasErrors()){
            Map<String, String> errors = result.getAllErrors().stream()
                    .collect(Collectors.toMap(k -> ((FieldError)k).getField(), v-> v.getDefaultMessage(), (r1, r2) -> r1));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(transactionService.buyTransaction(transactionDTO), HttpStatus.CREATED);
    }
}
