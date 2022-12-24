package chorestogetherapiservice.exception.mapper;

import javax.inject.Singleton;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
@Singleton
public class ConstraintViolationExceptionMapper implements
    ExceptionMapper<ConstraintViolationException> {

  private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeExceptionMapper.class);

  @Override
  public Response toResponse(ConstraintViolationException exception) {
    LOGGER.debug("ConstraintViolationException is invoked with the error message : ", exception);
    return Response.status(Response.Status.BAD_REQUEST)
        .entity("ConstraintViolationExceptionMapper raised "
            + "due to constraint violated. Violations : "
            + exception.toString())
        .build();
  }
}