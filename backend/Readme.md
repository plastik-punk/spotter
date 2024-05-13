# Backend Template for Spotter

## Start the backend
`mvn spring-boot:run`

## Start the backend with test data
If the database is not clean, the test data won't be inserted

`mvn spring-boot:run -Dspring-boot.run.profiles=generateData`

## Start Test Pipeline
Run complete pipeline test before pushing to Repository

`mvn test`
