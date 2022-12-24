package chorestogetherapiservice.exception.datastore;

import javax.ws.rs.WebApplicationException;

public class NoItemFoundException extends WebApplicationException {
  public NoItemFoundException(String message) {
    super(message);
  }
}
