package com.example.project.service;

import com.example.project.controller.FinancialTransactionController;
import com.example.project.models.*; // Ensure to import PaymentResponse
import com.example.project.repository.FinancialTransactionRepository;
import com.example.project.spec.TransactionQuerySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialTransactionService {
    private static final String ERROR_TRANSACTION_NULL = "Transaction cannot be null";
    private static final String ERROR_PAYMENT_NULL = "Payment cannot be null";

    private final FinancialTransactionRepository financialTransactionRepository;
    private final PaymentService paymentService;

    public Mono<ResponseEntity<DataListPaymentResponse>> getTransactions(TransactionQuerySpec transactionQuerySpec, Pageable pageable) {
        Page<Transaction> transactionPage = financialTransactionRepository.findAll(transactionQuerySpec, pageable);

        // Delegate to the private function to process the transactions
        return processTransactions(transactionPage);
    }


    private Mono<ResponseEntity<DataListPaymentResponse>> processTransactions(Page<Transaction> transactionPage) {
        // Step 1: Extract the list of transactions and convert to a Flux
        Flux<PaymentResponse> paymentResponsesFlux = Flux.fromIterable(transactionPage.getContent())
                // Step 2: Process each transaction
                .flatMap(transaction -> paymentService.retrieveFinancialTransaction(transaction.getPaymentId())
                        .map(payment -> assemblePaymentResponse(transaction, payment))
                        // Step 8: Error Handling
                        .onErrorResume(WebClientResponseException.class, ex -> {
                            System.out.println("Error retrieving payment for transaction ID" + transaction.getId() + ex.getMessage());
                            return Mono.empty(); // Skip this transaction
                        }));

        // Step 3: Collect, sort, and create response object
        return paymentResponsesFlux.collectList()
                .map(payments -> {
                    // Step 5: Sort payments by Payment ID in descending order
                    payments.sort(Comparator.comparing(PaymentResponse::getPaymentId).reversed());

                    // Step 6: Create response object with payments and links
                    DataListPaymentResponse response = new DataListPaymentResponse(
                            payments,
                            (HATEOASLinks) buildLinks(transactionPage) // Generate HATEOAS links
                    );
                    return ResponseEntity.ok(response); // Wrap in ResponseEntity
                })
                .onErrorResume(ex -> {
                    System.out.println("Error processing transactions " + ex.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
                }); // Handle any additional errors
    }

    private PaymentResponse assemblePaymentResponse(Transaction transaction, Payment payment) {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setTransaction(transaction);
        paymentResponse.setPaymentDetails(payment);
        return paymentResponse;
    }

    public List<Link> buildLinks(Page<Transaction> transactionPage) {
        List<Link> links = new ArrayList<>();

        // Add self link (link to the current page of transactions)
        links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FinancialTransactionController.class)
                        .getClass(/* necessary parameters, e.g., pagination params */))
                .withSelfRel());

        // Add link to the next page if it exists
        if (transactionPage.hasNext()) {
            links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FinancialTransactionController.class)
                            .getClass(/* parameters for next page */))
                    .withRel("next"));
        }

        // Add link to the previous page if it exists
        if (transactionPage.hasPrevious()) {
            links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FinancialTransactionController.class)
                            .getClass(/* parameters for previous page */))
                    .withRel("previous"));
        }

        // Add first page link
        if (!transactionPage.isFirst()) {
            links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FinancialTransactionController.class)
                            .getClass(/* parameters for first page */))
                    .withRel("first"));
        }

        // Add last page link
        if (!transactionPage.isLast()) {
            links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FinancialTransactionController.class)
                            .getClass(/* parameters for last page */))
                    .withRel("last"));
        }

        return links;
    }





//    public FinancialTransactionService(FinancialTransactionRepository financialTransactionRepository, PaymentService paymentService) {
//        this.financialTransactionRepository = financialTransactionRepository;
//        this.paymentService = paymentService;
//    }
//
//    public Mono<DataListPaymentResponse> getFilteredTransactions(String dateFrom, String dateTo,
//                                                                 String userId, String service,
//                                                                 String status, String reference,
//                                                                 Pageable pageable) {
//        return Mono.defer(() -> fetchAndProcessFinancialTransactions(dateFrom, dateTo, userId, service, status, reference, pageable));
//    }

//    private Mono<DataListPaymentResponse> fetchAndProcessFinancialTransactions(String dateFrom, String dateTo, String userId,
//                                                                               String service, String status, String reference,
//                                                                               Pageable pageable) {
//        Page<FinancialTransaction> transactionPage = financialTransactionRepository.findFilteredTransactions(
//                dateFrom, dateTo, userId, service, status, reference, pageable);
//
//        Flux<PaymentResponse> paymentsFlux = Flux.fromIterable(transactionPage.getContent())
//                .flatMap(transaction -> paymentService.retrieveFinancialTransaction(transaction.getPaymentId())
//                        .map(payment -> assemblePaymentResponse(transaction, payment)));
//
//        return paymentsFlux.collectList()
//                .map(payments -> {
//
//                    //I have comment out this because i didn't have time to create a constructor for it.
//                    payments.sort(Comparator.comparing(PaymentResponse::getPaymentId).reversed());
//                    return new DataListPaymentResponse(payments, buildLinks(transactionPage));
//                });
//    }

//    private Mono<DataListPaymentResponse> fetchAndProcessFinancialTransactions(String dateFrom, String dateTo, String userId,
//                                                                               String service, String status, String reference,
//                                                                               Pageable pageable) {
//        // Fetch filtered transactions with pagination
//        Page<FinancialTransaction> transactionPage = financialTransactionRepository.findFilteredTransactions(
//                dateFrom, dateTo, userId, service, status, reference, pageable);
//
//        // Process each transaction to retrieve corresponding payment details and assemble the response
//        Flux<PaymentResponse> paymentsFlux = Flux.fromIterable(transactionPage.getContent())
//                .flatMap(transaction -> paymentService.retrieveFinancialTransaction(transaction.getPaymentId())
//                        .map(payment -> assemblePaymentResponse(transaction, payment)));
//
//        // Collect responses into a list and build the final response
//        return paymentsFlux.collectList()
//                .map(payments -> {
//                    // Sort payments by paymentId in descending order
//                    payments.sort(Comparator.comparing(PaymentResponse::getPaymentId).reversed());
//
//                    // Construct and return the response object
//                    return new DataListPaymentResponse(
//                            payments,
//                            buildLinks(transactionPage)
//                    );
//                });
//    }
//
//
//    private PaymentResponse assemblePaymentResponse(FinancialTransaction transaction, Payment payment) {
//        if (transaction == null) {
//            throw new IllegalArgumentException(ERROR_TRANSACTION_NULL);
//        }
//
//        if (payment == null) {
//            throw new IllegalArgumentException(ERROR_PAYMENT_NULL);
//        }
//
//        PaymentResponse response = new PaymentResponse();
//        response.(transaction); // Updated setter
//        response.setPaymentDetails(payment);        // Updated setter
//        return response;
//    }
//
//    private HATEOASLinks buildLinks(Page<FinancialTransaction> transactionPage) {
//        return new HATEOASLinks(
//                "/api/financial-transactions?page=" + (transactionPage.getNumber() + 1),
//                transactionPage.getNumber() > 0 ? "/api/financial-transactions?page=" + (transactionPage.getNumber() - 1) : null,
//                "/api/financial-transactions?page=" + transactionPage.getNumber()
//        );
//    }
}
