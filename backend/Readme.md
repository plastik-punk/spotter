# Backend Template for Spotter

## Start the backend
`mvn spring-boot:run`

## Start the backend with test data
If the database is not clean, the test data won't be inserted

`mvn spring-boot:run -Dspring-boot.run.profiles=generateData`

## Start Test Pipeline
Run complete pipeline test before pushing to Repository

`mvn test`

## Conventions
### Naming DTOs
The name of a DTO always starts with the full name of the entity it represents (e.g. "MessageDetailedDto" representing "Message")

### Naming Services
The name of a service implementation equals the name of the interface with the suffix "Impl" (e.g. "MessageServiceImpl" implementing "MessageService")

### Interfaces
Interfaces are always at the top level of their package and the implementation is in a subpackage called "impl"

### Tests
- Each service implementation with an explicit implementation has a test class in the corresponding test package (unittests)
- Each endpoint has a test class in the corresponding test package (integrationtest)
- For each method in such classes implement:
  - a positive test
  - at least one negative test (cover all edge cases)
  - a test for each exception that is expected to be thrown