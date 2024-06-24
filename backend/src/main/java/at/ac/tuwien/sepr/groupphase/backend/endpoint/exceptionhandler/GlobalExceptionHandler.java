package at.ac.tuwien.sepr.groupphase.backend.endpoint.exceptionhandler;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.resterrors.ConflictErrorRestDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.resterrors.IllegalArgumentErrorRestDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.resterrors.NotFoundErrorRestDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.resterrors.ValidationErrorRestDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for the application.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Handles exceptions thrown when manually validating parameters (not via Valid annotation).
     * Sends a customized HTTP response for a validation exception as thrown by jakarta validation.
     *
     * @param e the exception
     * @return the error response
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleValidationException(ConstraintViolationException e) {
        LOGGER.warn("Terminating request processing with status 422 due to {}: {}", e.getClass().getSimpleName(), e.getMessage());

        List<String> errors = e.getConstraintViolations().stream()
            .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
            .collect(Collectors.toList());
        String defaultMessage = "Your request failed validation.";
        ValidationErrorRestDto errorResponse = new ValidationErrorRestDto(defaultMessage, errors);

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Handles exceptions thrown by annotation-style validation (not via Validators).
     * Sends a customized HTTP response for a validation exception as thrown by jakarta annotations.
     *
     * @param e the exception
     * @param headers the headers
     * @param status the status
     * @param request the request
     * @return the error response
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                               HttpHeaders headers,
                                                               HttpStatusCode status,
                                                               WebRequest request) {
        LOGGER.warn("Terminating request processing with status 422 due to {}: {}", e.getClass().getSimpleName(), e.getMessage());

        List<String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .collect(Collectors.toList());
        String defaultMessage = "Your request failed validation.";
        ValidationErrorRestDto errorResponse = new ValidationErrorRestDto(defaultMessage, errors);

        return new ResponseEntity<>(errorResponse, headers, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Handles exceptions thrown when a resource is not found.
     * Sends a customized HTTP response for a not found exception.
     *
     * @param ex the exception
     * @return the error response
     */
    @ExceptionHandler
    protected ResponseEntity<Object> handleNotFound(NotFoundException ex) {
        LOGGER.warn("Terminating request processing with status 404 due to {}: {}", ex.getClass().getSimpleName(), ex.getMessage());

        String defaultMessage = "The requested content was not found.";
        List<String> errors = List.of(ex.getMessage());
        NotFoundErrorRestDto errorResponse = new NotFoundErrorRestDto(defaultMessage, errors);

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exceptions thrown when a resource is not found.
     * Sends a customized HTTP response for a not found exception.
     *
     * @param e the exception
     * @return the error response
     */
    @ExceptionHandler
    protected ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
        LOGGER.warn("Terminating request processing with status 400 due to {}: {}", e.getClass().getSimpleName(), e.getMessage());

        String defaultMessage = "A provided argument was illegal.";
        List<String> errors = List.of(e.getMessage());
        IllegalArgumentErrorRestDto errorResponse = new IllegalArgumentErrorRestDto(defaultMessage, errors);

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions thrown when a resource is not found.
     * Sends a customized HTTP response for a not found exception.
     *
     * @param e the exception
     * @return the error response
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleConflictException(ConflictException e) {
        LOGGER.warn("Terminating request processing with status 409 due to {}: {}", e.getClass().getSimpleName(), e.getMessage());

        String defaultMessage = "Your request caused a conflict.";
        List<String> errors = List.of(e.getMessage());
        ConflictErrorRestDto errorResponse = new ConflictErrorRestDto(defaultMessage, errors);

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.CONFLICT);
    }
}