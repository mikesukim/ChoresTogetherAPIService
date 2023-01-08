package chorestogetherapiservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/**
 * Response which server will return to request, as Json format.
 * Its structure follows Jsend specification :
 * <a href=" https://github.com/omniti-labs/jsend"> Jsend specification </a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonResponse {

  @JsonProperty("status")
  String status;

  // when status is success, this value should be empty.
  @JsonProperty("message")
  String message;

  @JsonProperty("data")
  Map<String, String> data;

  //TODO: convert to Immutable
  /** default constructor. */
  public JsonResponse(String status, String message, Map<String, String> data) {
    this.status = status;
    this.message = message;
    this.data = data;
  }
}
