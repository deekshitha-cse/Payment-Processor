# WONDRx Payment Event Processor

Backend implementation for the WONDRx Java Backend Intern assignment.

## Tech Stack

* Java 21
* Spring Boot 4.1.1
* Spring Data JPA
* H2 In-Memory Database
* Maven
* JUnit 5

## Features

* Processes wallet DEBIT and CREDIT transactions
* Uses transactionId for idempotency
* Prevents duplicate transaction processing
* Uses database-level pessimistic locking for concurrent wallet updates
* Prevents wallet balances from becoming negative
* Includes integration tests for transaction processing, idempotency, and concurrent debit requests

## API

POST /api/v1/transactions/process

Example request:

json
{
  "transactionId": "11111111-1111-1111-1111-111111111111",
  "userId": "22222222-2222-2222-2222-222222222222",
  "amount": 100.00,
  "type": "DEBIT"
}


## Running Tests

The project uses the Maven Wrapper, so no global Maven installation is required.

### Windows

powershell
.\mvnw.cmd test

### Linux/macOS

bash
./mvnw test


The test suite uses an in-memory H2 database and requires no external database or service.

## Test Coverage

The integration tests cover:

* Processing a valid debit transaction successfully
* Sending three concurrent requests with the same transactionId and ensuring the wallet is deducted only once
* Sending ten concurrent debit requests against a wallet with ₹500 and ensuring exactly five succeed and five fail due to insufficient funds

See [`DECISIONS.md`](DECISIONS.md) for details about the concurrency and idempotency design decisions.
