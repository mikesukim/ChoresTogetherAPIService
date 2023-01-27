package chorestogetherapiservice.handler;

import chorestogetherapiservice.domain.ImmutableResponseEntity;
import chorestogetherapiservice.domain.ResponseEntity;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/** Generates response for the request.
 * Response's body/entity will be converted by Jackson from JAX-RS to Json.
 * Json structure follows Jsend specification.
 * <a href="https://github.com/omniti-labs/jsend"> Jsend specification </a>
 * */
@Singleton
@SuppressWarnings({"checkstyle:AbbreviationAsWordInName", "checkstyle:MemberName"})
public class ResponseHandler {

  private final int HTTP_OK_STATUS_CODE = 200;
  private final int HTTP_BAD_REQUEST_ERROR_STATUS_CODE = 400;
  private final int HTTP_NO_RESOURCE_FOUND_STATUS_CODE = 404;
  private final int HTTP_FAIL_STATUS_CODE = 500;
  private final String SUCCESS_STATUS = "success";
  private final String ERROR_STATUS = "error";
  private final String FAIL_STATUS = "fail";

  /** generate success Response without any data. */
  public Response generateSuccessResponse() {
    return generateJsonTypeResponseWith(HTTP_OK_STATUS_CODE, ImmutableResponseEntity
        .builder()
        .status(SUCCESS_STATUS)
        .build());
  }

  /** generate success Response with data. */
  public Response generateSuccessResponseWith(Object data) {
    return generateJsonTypeResponseWith(HTTP_OK_STATUS_CODE, ImmutableResponseEntity
        .builder()
        .status(SUCCESS_STATUS)
        .data(data)
        .build());
  }

  /** generate bad request error Response. */
  public Response generateBadRequestErrorResponseWith(String message) {
    return generateJsonTypeResponseWith(HTTP_BAD_REQUEST_ERROR_STATUS_CODE, ImmutableResponseEntity
        .builder()
        .status(ERROR_STATUS)
        .message(message)
        .build());
  }

  /** generate no resource item error Response. */
  public Response generateResourceNotFoundErrorResponseWith(String message) {
    return generateJsonTypeResponseWith(HTTP_NO_RESOURCE_FOUND_STATUS_CODE, ImmutableResponseEntity
        .builder()
        .status(ERROR_STATUS)
        .message(message)
        .build());
  }

  /** generate fail Response. */
  public Response generateFailResponseWith(String message) {
    return generateJsonTypeResponseWith(HTTP_FAIL_STATUS_CODE, ImmutableResponseEntity
        .builder()
        .status(FAIL_STATUS)
        .message(message)
        .build());
  }

  private Response generateJsonTypeResponseWith(int status, ResponseEntity responseEntity) {
    return Response
        .status(status)
        .entity(responseEntity)
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
