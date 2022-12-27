package chorestogetherapiservice.exception;

/**
 * this exception should be raised,
 * when dependency raises exception during the execution of our service's logic.
 * (e.x. dynamodb-client)
 */
public class DependencyFailureInternalException extends RuntimeException {
  public DependencyFailureInternalException(String message, Throwable cause) {
    super(message, cause);
  }
}
