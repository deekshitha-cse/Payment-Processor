package com.wondrx.paymentprocessor.service;

import com.wondrx.paymentprocessor.repository.WalletRepository;
import com.wondrx.paymentprocessor.dto.TransactionRequest;
import org.springframework.http.ResponseEntity;
import com.wondrx.paymentprocessor.entity.WalletEntity;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.wondrx.paymentprocessor.entity.TransactionType;
import com.wondrx.paymentprocessor.repository.TransactionRepository;
import com.wondrx.paymentprocessor.entity.TransactionEntity;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    public ResponseEntity<?> processTransaction(TransactionRequest request) {
        Optional<WalletEntity> wallet = walletRepository.findByUserId(request.getUserId());
        if(wallet.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found");
        }
        if(request.getType() == TransactionType.DEBIT) {
            if(request.getAmount().compareTo(wallet.get().getBalance()) > 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
            }
            wallet.get().setBalance(wallet.get().getBalance().subtract(request.getAmount()));
        } else if(request.getType() == TransactionType.CREDIT) {
            wallet.get().setBalance(wallet.get().getBalance().add(request.getAmount()));
        }

        TransactionEntity transaction = new TransactionEntity(request.getTransactionId(), request.getUserId(), request.getAmount(), request.getType());

        transactionRepository.save(transaction);

        return ResponseEntity.ok().build();
    }

}