package com.example.project.models;

import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor
//public class PaymentResponse {
//    private FinancialTransaction transactionDetails;
//    private Payment paymentDetails;
//
//    // Getters and Setters
//    public FinancialTransaction getTransactionDetails() {
//        return transactionDetails;
//    }
//
//    public void setTransactionDetails(FinancialTransaction transactionDetails) {
//        this.transactionDetails = transactionDetails;
//    }
//
//    public Payment getPaymentDetails() {
//        return paymentDetails;
//    }
//
//    public void setPaymentDetails(Payment paymentDetails) {
//        this.paymentDetails = paymentDetails;
//    }
//
//    public String getPaymentId() {
//        if (paymentDetails != null) {
//            return paymentDetails.getPaymentId(); // Assuming Payment class has getPaymentId
//        }
//        if (transactionDetails != null) {
//            return transactionDetails.getPaymentId(); // Assuming FinancialTransaction class has getPaymentId
//        }
//        return null; // Fallback if both are null
//    }
//}


import lombok.Data;

@Data
@RequiredArgsConstructor
public class PaymentResponse {
    private Transaction transaction;
    private Payment paymentDetails;


    public String getPaymentId() {
        if (paymentDetails != null) {
            return paymentDetails.getPaymentId();
        }
        if (transaction != null) {
            return transaction.getPaymentId();
        }
        return null;
    }
}
