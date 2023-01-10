package chorestogetherapiservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.Optional;

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
  /** JsonResponse constructor.
   *  If message or data is empty,
   *  that field will not be present at the Json.
   * */
  public JsonResponse(String status,
                      Optional<String> optionalMessage,
                      Optional<Map<String, String>> optionalData) {
    this.status = status;
    this.message = optionalMessage.orElse(null);
    this.data = optionalData.orElse(null);
  }
}
