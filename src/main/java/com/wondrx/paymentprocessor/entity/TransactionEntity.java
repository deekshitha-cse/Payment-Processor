package com.wondrx.paymentprocessor.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity
public class TransactionEntity {

    @Id
    private UUID transactionId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;
}