package chorestogetherapiservice.exception.mapper;

import chorestogetherapiservice.handler.ResponseHandler;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * When ConstraintViolationException occurs due to validation failure,
 * then this mapper will be executed and return response.
 */
@Provider
public class ConstraintViolationExceptionMapper implements
    ExceptionMapper<ConstraintViolationException> {

  private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeExceptionMapper.class);

  ResponseHandler responseHandler;

  @Inject
  public ConstraintViolationExceptionMapper(ResponseHandler responseHandler) {
    this.responseHandler = responseHandler;
  }

  @Override
  public Response toResponse(ConstraintViolationException exception) {
    LOGGER.debug("ConstraintViolationException is invoked with the error message : ", exception);
    return responseHandler.generateErrorResponseWith("request constraint violated");
  }
}