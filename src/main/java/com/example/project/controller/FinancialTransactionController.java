package com.example.project.controller;

import com.example.project.service.FinancialTransactionService;
import com.example.project.models.DataListPaymentResponse;
import com.example.project.enums.TransactionStatus;
import com.example.project.spec.TransactionQuerySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class FinancialTransactionController {

    private final FinancialTransactionService transactionService;

    @GetMapping("/api/financial-transactions")
    public Mono<ResponseEntity<ResponseEntity<DataListPaymentResponse>>> fetchTransactions(
            TransactionQuerySpec transactionQuerySpec, Pageable pageable) {
        System.out.println("Incoming Request :: " + transactionQuerySpec);
        return transactionService.getTransactions(transactionQuerySpec, pageable)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, e ->
                        Mono.just(ResponseEntity.status(e.getStatusCode()).body(null))
                );
    }
}

