package chorestogetherapiservice.exception.mapper;

import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * When RuntimeException occurs during the service handling the request,
 * then this mapper will be executed and return response.
 *
 * <br>
 * '@Provider' is required for
 * <a href="https://docs.oracle.com/javaee/7/api/javax/ws/rs/ext/ExceptionMapper.html">ExceptionMapper</a>
 * to be detected by JAX-RS runtime.
 */
@Provider
@Singleton
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

  private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeExceptionMapper.class);

  @Override
  public Response toResponse(RuntimeException exception) {
    LOGGER.debug("ExceptionMapper is invoked with the error message : ", exception);
    int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    if (exception instanceof WebApplicationException) {
      WebApplicationException webApplicationException = (WebApplicationException) exception;
      status = webApplicationException.getResponse().getStatus();
    }
    return Response
        .status(status)
        // TODO: expose stackTrace only for development.
        .entity("RuntimeExceptionMapper raised due to RuntimeException occurred. error message: "
            + exception.getMessage())
        .type(MediaType.APPLICATION_JSON_TYPE)
        .build();
  }
}
