package at.ac.tuwien.sepr.groupphase.backend.exception;

/**
 * Exception that signals, that the credentials provided by the user are invalid.
 */
public class BadCredentialsException extends RuntimeException {

    public BadCredentialsException(String message) {
        super(message);
    }

    public BadCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
