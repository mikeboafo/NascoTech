package com.example.project.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class FinancialTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentId;
    private String userId;
    private String service;
    private String status;
    private String reference;
    private LocalDate date;

    // Getter for paymentId
    public String getPaymentId() {
        return paymentId;  // Returning the actual paymentId
    }

    // Setter for paymentId
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    // Getter for id
    public Long getId() {
        return id;
    }

    // Setter for id
    public void setId(Long id) {
        this.id = id;
    }

    // Getter for userId
    public String getUserId() {
        return userId;
    }

    // Setter for userId
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter for service
    public String getService() {
        return service;
    }

    // Setter for service
    public void setService(String service) {
        this.service = service;
    }

    // Getter for status
    public String getStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(String status) {
        this.status = status;
    }

    // Getter for reference
    public String getReference() {
        return reference;
    }

    // Setter for reference
    public void setReference(String reference) {
        this.reference = reference;
    }

    // Getter for date
    public LocalDate getDate() {
        return date;
    }

    // Setter for date
    public void setDate(LocalDate date) {
        this.date = date;
    }
}
