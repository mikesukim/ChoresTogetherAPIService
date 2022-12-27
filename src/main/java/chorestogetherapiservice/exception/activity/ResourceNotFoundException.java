package chorestogetherapiservice.exception.activity;

import javax.ws.rs.WebApplicationException;

/**
 * this exception should only be raised at Activity level,
 * when the result is empty or no such request's endpoint exist.
 */
public class ResourceNotFoundException extends WebApplicationException {
  public ResourceNotFoundException(String message, Throwable cause) {
    super(message, cause, 404);
  }
}
