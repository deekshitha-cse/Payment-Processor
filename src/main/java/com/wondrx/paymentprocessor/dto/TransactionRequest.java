package com.wondrx.paymentprocessor.dto;

import com.wondrx.paymentprocessor.entity.TransactionType;

import java.util.UUID;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

public class TransactionRequest {

    @NotNull
    private UUID transactionId;

    @NotNull
    private UUID userId;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @NotNull
    private TransactionType type;

    public UUID getTransactionId() {
        return transactionId;
    }

    public UUID getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }
}