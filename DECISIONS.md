# Design Decisions

## 1. Concurrency Control

The main concurrency risk is two or more debit requests reading the same wallet balance at the same time before either request updates it which may result in incorrect balance updates.

For example if a wallet has ₹100 and two concurrent requests each attempt to debit ₹100 from the same wallet at the same time, both requests could read the balance as ₹100 and both could proceed without concurrency control.

To prevent this the wallet lookup uses a JPA PESSIMISTIC_WRITE lock. The transaction-processing method is also annotated with @Transactional.

The pessimistic lock ensures that only one transaction can modify the wallet row at a time. The transaction keeps the lock while the balance is checked, updated, and the transaction record is saved. Other concurrent requests wait until the current transaction completes before accessing the locked wallet.

This prevents concurrent debit operations from using the same stale balance and prevents the wallet from becoming negative.

## 2. Idempotency

The transactionId is used as the idempotency key. A transaction with an already processed ID must not modify the wallet again. The same transactionId should not be processed more than once.

The service performs an initial check to detect transactions that have already been processed.

For concurrent requests with the same transactionId, multiple requests may initially see that the transaction does not exist because the first request has not committed yet.

Therefore, after acquiring the wallet's pessimistic lock the service performs a second idempotency check to determine whether another concurrent request has already processed the transaction.

The first request that acquires the lock processes the transaction and saves it. When the other requests subsequently acquire the lock, they perform the second check and find that the transaction ID already exists. They then return 409 CONFLICT without modifying the wallet.

The second check is necessary because concurrent requests may all initially see that the transaction does not exist. After acquiring the lock we check again to see if the transaction has already been processed.

This ensures that the balance is updated only once for concurrent requests using the same transaction ID.

## 3. AI-Assisted Development

AI assistance was used during development but the suggestions were reviewed against the actual application behavior.

One example occurred while writing the happy-path integration test. The test returned 404 NOT_FOUND with Wallet not found. An initial AI suggestion focused on checking whether the userId values matched.

Reviewing the test code showed that the actual issue was that the wallet entity had been created in Java but had not been persisted to the database using walletRepository.save(wallet).

After adding the repository save operation the test passed.

This demonstrated the importance of validating AI suggestions against the actual database and application flow rather than accepting them without verification.

## 4. Trade-offs

Pessimistic locking was chosen because the assignment specifically requires database-level protection against concurrent wallet debits.

It provides a straightforward way to serialize updates to the same wallet and avoids implementing an optimistic-locking retry mechanism for this assignment.

The trade-off is that concurrent requests targeting the same wallet may have to wait for one another which can reduce concurrency for that wallet. For the scope of this assignment correctness and preventing an invalid balance were prioritized.
