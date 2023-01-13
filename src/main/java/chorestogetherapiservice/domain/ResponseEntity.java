package chorestogetherapiservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Map;
import javax.annotation.Nullable;
import org.immutables.value.Value;

/**
 * Response which server will return to request, as Json format.
 * Its structure follows Jsend specification :
 * <a href=" https://github.com/omniti-labs/jsend"> Jsend specification </a>
 */
@Value.Immutable
@JsonSerialize(as = ImmutableResponseEntity.class)
@JsonInclude(JsonInclude.Include.NON_NULL) // variable will not be included at Json
public interface ResponseEntity {

  String getStatus();
  // when status is error & fail, message should not be null

  @Nullable
  String getMessage();

  // when status is error & fail, data should be null
  @Nullable
  Map<String, String> getData();
}
