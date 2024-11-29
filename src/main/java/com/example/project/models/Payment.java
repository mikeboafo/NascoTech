package com.example.project.models;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Payment {
    private String paymentId;
    private String status;
    private String method;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
}
