package com.example.project.service;

import com.example.project.models.Payment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PaymentService {

    private final WebClient webClient;

    public PaymentService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://payment-service").build();
    }

    public Mono<Payment> retrieveFinancialTransaction(String paymentId) {
        return webClient.get()
                .uri("/payments/{id}", paymentId)
                .retrieve()
                .bodyToMono(Payment.class);
    }
}
