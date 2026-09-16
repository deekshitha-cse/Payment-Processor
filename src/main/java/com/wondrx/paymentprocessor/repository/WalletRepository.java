package com.wondrx.paymentprocessor.repository;

import com.wondrx.paymentprocessor.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<WalletEntity> findByUserId(UUID userId);
}