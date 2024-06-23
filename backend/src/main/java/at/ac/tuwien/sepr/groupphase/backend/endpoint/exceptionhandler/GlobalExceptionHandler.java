package at.ac.tuwien.sepr.groupphase.backend.endpoint.exceptionhandler;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.resterrors.ConflictErrorRestDto;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Register all your Java exceptions here to map them into meaningful HTTP exceptions
 * If you have special cases which are only important for specific endpoints, use ResponseStatusExceptions
 * https://www.baeldung.com/exception-handling-for-rest-with-spring#responsestatusexception
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Handles exceptions thrown when manually validating parameters (not via Valid annotation).
     *
     * @param e the exception
     * @return the error response
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ValidationErrorRestDto handleValidationException(ConstraintViolationException e) {
        LOGGER.warn("Terminating request processing with status 422 due to {}: {}", e.getClass().getSimpleName(), e.getMessage());
        List<String> errors = e.getConstraintViolations().stream()
            .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
            .collect(Collectors.toList());

        return new ValidationErrorRestDto(e.getMessage(), errors);
    }

    /**
     * Use the @ExceptionHandler annotation to write handler for custom exceptions.
     */
    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        LOGGER.warn(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Use the @ExceptionHandler annotation to write handler for custom exceptions.
     */
    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalArgument(RuntimeException ex, WebRequest request) {
        LOGGER.warn(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Override methods from ResponseEntityExceptionHandler to send a customized HTTP response for a validation exception as thrown by jakarta annotations.
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                               HttpHeaders headers,
                                                               HttpStatusCode status, WebRequest request) {
        LOGGER.warn("Terminating request processing with status 422 due to {}: {}", e.getClass().getSimpleName(), e.getMessage());
        //Get all errors
        List<String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getField() + " " + err.getDefaultMessage())
            .collect(Collectors.toList());

        ValidationErrorRestDto errorResponse = new ValidationErrorRestDto(e.getMessage(), errors);

        return new ResponseEntity<>(errorResponse, headers, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ConflictErrorRestDto handleConflictException(ConflictException e) {
        LOGGER.warn("Terminating request processing with status 409 due to {}: {}", e.getClass().getSimpleName(), e.getMessage());
        return new ConflictErrorRestDto(e.summary(), e.errors());
    }
}