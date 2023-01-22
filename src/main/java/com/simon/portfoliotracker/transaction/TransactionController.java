package com.simon.portfoliotracker.transaction;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionRead>> getTransactions(){
        return new ResponseEntity<>(transactionService.getTransactions(), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Object> addTransaction(@Valid @RequestBody TransactionWrite transactionDTO,
                                                  BindingResult result){
        if(result.hasErrors()){
            Map<String, String> errors = result.getAllErrors().stream()
                    .collect(Collectors.toMap(k -> ((FieldError)k).getField(), v-> v.getDefaultMessage(), (r1, r2) -> r1));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(transactionService.buyTransaction(transactionDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id){
        try {
            transactionService.deleteTransaction(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> markAsCompleted(@PathVariable Long id, @RequestBody Double sellingPrice){
            return new ResponseEntity<>(transactionService.sellTransaction(id, sellingPrice), HttpStatus.OK);
    }

}
