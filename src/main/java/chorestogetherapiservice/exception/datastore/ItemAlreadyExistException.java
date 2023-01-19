package chorestogetherapiservice.exception.datastore;

import javax.ws.rs.WebApplicationException;

/** exception when new item is already exist at database. */
public class ItemAlreadyExistException extends WebApplicationException {
  public ItemAlreadyExistException(String message) {
    super(message);
  }
}
