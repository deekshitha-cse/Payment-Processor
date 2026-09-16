package com.wondrx.paymentprocessor.repository;

import com.wondrx.paymentprocessor.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {
    
}