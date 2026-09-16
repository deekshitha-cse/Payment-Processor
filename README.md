# WONDRx Payment Event Processor

Backend implementation for the WONDRx Java Backend Intern assignment.

## Tech Stack

- Java 21
- Spring Boot 4.1.1
- Spring Data JPA
- H2 In-Memory Database
- Maven
- JUnit 5

## Running Tests

The project uses the Maven Wrapper, so no global Maven installation is required.

### Windows

powershell
.\mvnw.cmd test

### Linux/macOS

bash
./mvnw test

The test suite uses an in-memory H2 database and requires no external database or service.