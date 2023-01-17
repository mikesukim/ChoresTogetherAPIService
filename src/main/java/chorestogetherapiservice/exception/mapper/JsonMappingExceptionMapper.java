package chorestogetherapiservice.exception.mapper;

import chorestogetherapiservice.handler.ResponseHandler;
import com.fasterxml.jackson.databind.JsonMappingException;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * When JsonMappingException occurs due to validation failure,
 * then this mapper will be executed and return response.
 * Purpose of Priority(1) annotation is to mask jersey-media-json-jackson's
 * JsonMappingExceptionMapper, to provide uniform response.
 * <a href="https://github.com/FasterXML/jackson-jaxrs-providers/issues/22">related link</a>
 */
@Priority(1)
@Provider
public class JsonMappingExceptionMapper implements
    ExceptionMapper<JsonMappingException> {

  ResponseHandler responseHandler;

  @Inject
  public JsonMappingExceptionMapper(ResponseHandler responseHandler) {
    this.responseHandler = responseHandler;
  }

  @Override
  public Response toResponse(JsonMappingException exception) {
    return responseHandler.generateBadRequestErrorResponseWith(exception.getMessage());
  }
}