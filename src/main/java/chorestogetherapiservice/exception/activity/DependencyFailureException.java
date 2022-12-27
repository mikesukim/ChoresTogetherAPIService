package chorestogetherapiservice.exception.activity;

/**
 * dependency failure exception, only to be raised only at Activity level.
 * when dependency related exception raises (e.x. DependencyFailureInternalException),
 * then those exceptions should be transformed into this exception at activity level.
 */
public class DependencyFailureException extends RuntimeException {
  public DependencyFailureException(String message, Throwable cause) {
    super(message, cause);
  }
}
