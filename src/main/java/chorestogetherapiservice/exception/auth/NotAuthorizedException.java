package chorestogetherapiservice.exception.auth;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/** exception to be raised when API request call's authentication fails. */
public class NotAuthorizedException extends WebApplicationException {
  public NotAuthorizedException(String message) {
    super(message, Response.Status.BAD_REQUEST.getStatusCode());
  }
}
