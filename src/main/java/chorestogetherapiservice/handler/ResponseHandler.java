package chorestogetherapiservice.handler;

import chorestogetherapiservice.domain.JsonResponse;
import java.util.Map;
import java.util.Optional;
import javax.inject.Singleton;
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
  private final int HTTP_FAIL_STATUS_CODE = 500;
  private final String SUCCESS_STATUS = "success";
  private final String ERROR_STATUS = "error";
  private final String FAIL_STATUS = "fail";

  /** generate success Response without any data. */
  public Response generateSuccessResponse() {
    return Response
        .status(HTTP_OK_STATUS_CODE)
        .entity(
            new JsonResponse(
                SUCCESS_STATUS, Optional.empty(), Optional.empty())
        ).build();
  }

  /** generate success Response with data. */
  public Response generateSuccessResponseWith(Map<String, String> data) {
    return Response
        .status(HTTP_OK_STATUS_CODE)
        .entity(
            new JsonResponse(
                SUCCESS_STATUS, Optional.empty(), Optional.of(data))
        ).build();
  }

  /** generate error Response. */
  public Response generateErrorResponseWith(String message) {
    return Response
        // TODO:
        //  now all error response is treated as 400 status code.
        //  Update to return other error status codes as well,
        //  which can describe the actual error reason.
        .status(HTTP_BAD_REQUEST_ERROR_STATUS_CODE)
        .entity(
            new JsonResponse(
                ERROR_STATUS, Optional.of(message), Optional.empty())
        ).build();
  }

  /** generate fail Response. */
  public Response generateFailResponseWith(String message) {
    return Response
        .status(HTTP_FAIL_STATUS_CODE)
        .entity(
            new JsonResponse(
                FAIL_STATUS, Optional.of(message), Optional.empty())
        ).build();
  }
}
