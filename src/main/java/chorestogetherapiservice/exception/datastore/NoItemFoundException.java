package chorestogetherapiservice.exception.datastore;

import javax.ws.rs.WebApplicationException;

/** exception when no result was found at database. (e.x. no such userEmail exists in database) */
public class NoItemFoundException extends WebApplicationException {
  public NoItemFoundException(String message) {
    super(message);
  }
}
