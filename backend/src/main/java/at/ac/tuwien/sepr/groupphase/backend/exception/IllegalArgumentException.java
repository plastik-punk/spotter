package at.ac.tuwien.sepr.groupphase.backend.exception;

/**
 * Exception that signals, that a method was called with an illegal or inappropriate argument.
 */
public class IllegalArgumentException extends RuntimeException {

    public IllegalArgumentException(String message) {
        super(message);
    }

    public IllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
