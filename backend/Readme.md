# Backend Template for Spotter

## Start the backend

`mvn spring-boot:run`

## Start the backend with test data

If the database is not clean, the test data won't be inserted.

`mvn spring-boot:run -Dspring-boot.run.profiles=generateData`

## Start Test Pipeline

Run complete pipeline test before pushing to Repository

`mvn test`

## Install Dependencies

If new dependencies were added in the pom.xml file

`mvn clean install`

## Defining Validation Rules

### Standard Annotations (for DTOs and Entities)

- Both DTOs and Entities should use standard validation annotations
- some basic annotations are: @NotNull, @Size, @Email, @Pattern, @Min, @Max, @Positive...

### Custom Annotations

- used for complex validation rules
- placed in the "validation" package
- instead of annotating a specific field, a custom annotation is used to validate the whole object
- for an example, see EventCreateDto and the custom annotation @EndDateAndTimeAfterStartValidation

## Using Validation

Generally, use @Valid annotation and jakarta Validator, no custom validators!

NOTE: the @Valid annotation can only be used on endpoint-parameters

### Validate DTOs in endpoint (s. ReservationEndpoint.create for an example)

- simply use @Valid on the parameter

### Validation in service layer (s. ReservationServiceImpl for an example)

1. create a private field of type jakarta.validation.Validator
2. create and fill a Set with ConstraintViolation-objects by calling validator.validate(dto/entity)
3. if the set is not empty throw a ConstraintViolationException with the set as parameter

## Conventions

### Naming DTOs

The name of a DTO always starts with the full name of the entity it represents (e.g. "MessageDetailedDto" representing "
Message")

### Naming Services

The name of a service implementation equals the name of the interface with the suffix "Impl" (e.g. "MessageServiceImpl"
implementing "MessageService")

### Interfaces

Interfaces are always at the top level of their package and the implementation is in a subpackage called "impl"

## DataGen
- The order of data generation is defined via dependency-injection
- Any data-generator that is injected into another data generator will be executed first
- Multiple data-generators can be injected into one data-generator
- this order by injection can have multiple levels and works like a tree, conceptually, allowing precise control over
  the order of data generation
- @Order only defines the order of bean-creation, not the order of execution and is only used to visualize the order in a more readable manner

### Example: ReservationDataGenerator:
1. ReservationDataGenerator injects the data-generators for place and user by using them as parameters in the constructor (no need for initialization)
2. Any data-generator that is injected in such a way is executed before the data-generator that injects it
   - place and user data-generators are executed before the reservation data-generator
   - place could inject user (or vice versa) to further define their order of execution if necessary

## Tests

### Naming
- givenX_whenY_thenZ (e.g. givenNothing_whenCreate_then400())
    - X: the input data or special conditions
    - Y: method name + optional test characteristics
    - Z: what is the expected result

### Test Data
- use explicit test data defined in /basetest/TestData (assertions test against these)

### test service implementations
- Each service has a test class in the corresponding package of "unittests"
- use @Transactional for tests that write to the database (e.g. save())
- test each method in isolation with:
  - a positive test
  - a negative test for each expected exception (except ValidationExceptions)

### test entities
- Each entity has a test class in the corresponding package of "unittests"
- test positive for:
  - equals()
  - build()
  - toString()
  - hashCode()

### test repositories
- Each repository has a test class in the corresponding package of "unittests"
- use @Transactional for tests when they contain a save() call
- test positive for custom queries and methods (no autogenerated methods)

### test endpoints
- Each endpoint has a test class in the corresponding package of "integrationtest"
- use @Transactional for each test
- test positive for each method, check for:
  - status code
  - response body