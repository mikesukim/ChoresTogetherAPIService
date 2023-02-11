package chorestogetherapiservice.exception.dependency;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * this exception should be raised,
 * when dependency raises exception during the execution of our service's logic.
 * (e.x. dynamodb-client)
 */
public class DependencyFailureInternalException extends WebApplicationException {
  public DependencyFailureInternalException(String message, Throwable cause) {
    super(message, cause, Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
  }
}
