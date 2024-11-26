package com.example.project.Models;

public class PaymentResponse {
    private FinancialTransaction transactionDetails;
    private Payment paymentDetails;

    // Getters and Setters
    public FinancialTransaction getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(FinancialTransaction transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public Payment getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(Payment paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
}
