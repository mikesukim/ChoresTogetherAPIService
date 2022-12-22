package chorestogetherapiservice.exception.activity;

import javax.ws.rs.WebApplicationException;

public class ResourceNotFoundException extends WebApplicationException {
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause, 404);
    }
}
