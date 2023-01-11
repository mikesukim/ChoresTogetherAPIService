package chorestogetherapiservice.exception.mapper;

import chorestogetherapiservice.handler.ResponseHandler;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
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
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

  private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeExceptionMapper.class);

  ResponseHandler responseHandler;

  @Inject
  public RuntimeExceptionMapper(ResponseHandler responseHandler) {
    this.responseHandler = responseHandler;
  }

  @Override
  public Response toResponse(RuntimeException exception) {
    LOGGER.debug("ExceptionMapper is invoked with the error message : ", exception);
    int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    if (exception instanceof WebApplicationException) {
      WebApplicationException webApplicationException = (WebApplicationException) exception;
      status = webApplicationException.getResponse().getStatus();
    }
    return responseHandler.generateFailResponseWith("runtime exception occurred");
  }
}
