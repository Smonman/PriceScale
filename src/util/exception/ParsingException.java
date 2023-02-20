package util.exception;

/**
 * This is an exception.
 *
 * <p>This exception is throws, when something cannot be parsed as
 * expected.</p>
 */
public class ParsingException extends Exception {

    public ParsingException() {
        super();
    }

    public ParsingException(String message) {
        super(message);
    }

    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParsingException(Throwable cause) {
        super(cause);
    }
}
