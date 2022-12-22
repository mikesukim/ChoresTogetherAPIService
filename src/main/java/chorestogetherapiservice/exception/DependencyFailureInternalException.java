package chorestogetherapiservice.exception;

public class DependencyFailureInternalException extends RuntimeException {
    public DependencyFailureInternalException(String message, Throwable cause) {
        super(message, cause);
    }
}
