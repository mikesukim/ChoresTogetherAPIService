package chorestogetherapiservice.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/** Token to be used for authentication check for API call.
 *
 * <p>
 * Once user is logged into our service successfully (e.x. through login API, login/google),
 * then this token is generated and returned back to user.
 * Then, every API requests should include this token at header.
 * Then token will be validated before executing API's logic for API authentication check.
 * </p> */
@Value.Immutable
@JsonSerialize(as = ImmutableToken.class)
public interface Token {
  String getToken();
}
