package com.example.project.models;

import java.util.List;

public class DataListPaymentResponse {
    private List<PaymentResponse> payments;
    private HATEOASLinks links;

    public DataListPaymentResponse(List<PaymentResponse> payments, HATEOASLinks links) {
        this.payments = payments;
        this.links = links;
    }

    public List<PaymentResponse> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentResponse> payments) {
        this.payments = payments;
    }

    public HATEOASLinks getLinks() {
        return links;
    }

    public void setLinks(HATEOASLinks links) {
        this.links = links;
    }
}

