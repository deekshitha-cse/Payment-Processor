package com.wondrx.paymentprocessor.controller;

import com.wondrx.paymentprocessor.dto.TransactionRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.wondrx.paymentprocessor.service.TransactionService;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/process")
    public ResponseEntity<?> processTransaction(@Valid @RequestBody TransactionRequest request) {
        return transactionService.processTransaction(request);
    }
}