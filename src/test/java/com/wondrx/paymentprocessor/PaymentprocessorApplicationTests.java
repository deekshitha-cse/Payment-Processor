package com.wondrx.paymentprocessor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import com.wondrx.paymentprocessor.repository.WalletRepository;
import com.wondrx.paymentprocessor.entity.WalletEntity;
import com.wondrx.paymentprocessor.service.TransactionService;
import com.wondrx.paymentprocessor.dto.TransactionRequest;
import com.wondrx.paymentprocessor.entity.TransactionType;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PaymentprocessorApplicationTests {

	private final WalletRepository walletRepository;
	private final TransactionService transactionService;

	PaymentprocessorApplicationTests(WalletRepository walletRepository, TransactionService transactionService) {
		this.walletRepository = walletRepository;
		this.transactionService = transactionService;
	}

      
	@Test
	@DisplayName("Loads Spring Application context successfully")
	void contextLoads() {
	}

	@Test
	@DisplayName("Sends 10 concurrent debit requests ...")
	void concurrentDebitTest() {
		
		UUID userId = UUID.randomUUID();
		BigDecimal amount = new BigDecimal("500.00");

		WalletEntity wallet = new WalletEntity(userId, amount);
		walletRepository.save(wallet);

		ExecutorService executor = Executors.newFixedThreadPool(10);
		List<Future<?>> futures = new ArrayList<>();

		for(int i = 0; i < 10; i++) {
			Future<?> future = executor.submit(() -> {
				TransactionRequest request = new TransactionRequest(UUID.randomUUID(), userId, new BigDecimal("100.00"), TransactionType.DEBIT);
				transactionService.processTransaction(request);
			});
			futures.add(future);
		}

		int failed = 0;

		for(Future<?> future: futures) {
			try{
				future.get();
			} catch (ExecutionException e) {
				Throwable cause = e.getCause();
				assertTrue(cause instanceof ResponseStatusException);

				ResponseStatusException exception = (ResponseStatusException) cause;
				assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
				failed++;
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException(e);
			}
		}

		executor.shutdown();
		assertEquals(5, failed);


		WalletEntity updatedWallet = walletRepository.findWalletForRead(userId).orElseThrow();
		assertEquals(new BigDecimal("0.00"), updatedWallet.getBalance());
	}

	@Test
	@DisplayName("Processes a single valid debit transaction successfully.")
	void happyPathTest() {
		UUID userId = UUID.randomUUID();
		BigDecimal amount = new BigDecimal("500.00");
		WalletEntity wallet = new WalletEntity(userId, amount);
		walletRepository.save(wallet);
		TransactionRequest transaction = new TransactionRequest(UUID.randomUUID(), userId, new BigDecimal("100"), TransactionType.DEBIT);
		ResponseEntity<?> response = transactionService.processTransaction(transaction);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		WalletEntity updatedWallet = walletRepository.findWalletForRead(userId).orElseThrow();
		assertEquals(new BigDecimal("400.00"), updatedWallet.getBalance());
	}
}
