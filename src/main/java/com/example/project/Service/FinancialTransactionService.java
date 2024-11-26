package com.example.project.Service;

import com.example.project.Models.*; // Ensure to import PaymentResponse
import com.example.project.Repository.FinancialTransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@Service
public class FinancialTransactionService {
    private static final String ERROR_TRANSACTION_NULL = "Transaction cannot be null";
    private static final String ERROR_PAYMENT_NULL = "Payment cannot be null";

    private final FinancialTransactionRepository repository;
    private final PaymentService paymentService;

    public FinancialTransactionService(FinancialTransactionRepository repository, PaymentService paymentService) {
        this.repository = repository;
        this.paymentService = paymentService;
    }

    public Mono<DataListPaymentResponse> getFilteredTransactions(String dateFrom, String dateTo,
                                                                 String userId, String service,
                                                                 String status, String reference,
                                                                 Pageable pageable) {
        return Mono.defer(() -> fetchAndProcessFinancialTransactions(dateFrom, dateTo, userId, service, status, reference, pageable));
    }

    private Mono<DataListPaymentResponse> fetchAndProcessFinancialTransactions(String dateFrom, String dateTo, String userId,
                                                                               String service, String status, String reference,
                                                                               Pageable pageable) {
        Page<FinancialTransaction> transactionPage = repository.findFilteredTransactions(
                dateFrom, dateTo, userId, service, status, reference, pageable);

        Flux<PaymentResponse> paymentsFlux = Flux.fromIterable(transactionPage.getContent())
                .flatMap(transaction -> paymentService.retrieveFinancialTransaction(transaction.getPaymentId())
                        .map(payment -> assemblePaymentResponse(transaction, payment)));

        return paymentsFlux.collectList()
                .map(payments -> {

                    //I have comment out this because i didn't have time to create a constructor for it.
                   // payments.sort(Comparator.comparing(PaymentResponse::getPaymentId).reversed());
                    return new DataListPaymentResponse(payments, buildLinks(transactionPage));
                });
    }

    private PaymentResponse assemblePaymentResponse(FinancialTransaction transaction, Payment payment) {
        if (transaction == null) {
            throw new IllegalArgumentException(ERROR_TRANSACTION_NULL);
        }

        if (payment == null) {
            throw new IllegalArgumentException(ERROR_PAYMENT_NULL);
        }

        PaymentResponse response = new PaymentResponse();
        response.setTransactionDetails(transaction); // Updated setter
        response.setPaymentDetails(payment);        // Updated setter
        return response;
    }

    private HATEOASLinks buildLinks(Page<FinancialTransaction> transactionPage) {
        return new HATEOASLinks(
                "/api/financial-transactions?page=" + (transactionPage.getNumber() + 1),
                transactionPage.getNumber() > 0 ? "/api/financial-transactions?page=" + (transactionPage.getNumber() - 1) : null,
                "/api/financial-transactions?page=" + transactionPage.getNumber()
        );
    }
}
