package chorestogetherapiservice.exception.datastore;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/** exception when new item is already exist at database. */
public class ItemAlreadyExistException extends WebApplicationException {
  public ItemAlreadyExistException(String message) {
    super(message, Response.Status.BAD_GATEWAY.getStatusCode());
  }
}
