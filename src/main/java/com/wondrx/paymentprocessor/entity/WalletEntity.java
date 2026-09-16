package com.wondrx.paymentprocessor.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.util.UUID;
import java.math.BigDecimal;
import jakarta.persistence.Column;

@Entity
public class WalletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    private BigDecimal balance;

    protected WalletEntity() {

    }

    public WalletEntity(UUID userId, BigDecimal balance) {
        this.userId = userId;
        this.balance = balance;
    } 

    public UUID getId() {
        return id;
    }


    public UUID getUserId() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}