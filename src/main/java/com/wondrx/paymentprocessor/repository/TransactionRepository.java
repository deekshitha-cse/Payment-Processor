package com.wondrx.paymentprocessor.repository;

import com.wondrx.paymentprocessor.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {
    
}