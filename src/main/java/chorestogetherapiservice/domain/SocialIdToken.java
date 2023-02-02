package chorestogetherapiservice.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

/** ID token which is generated by 3rd party service (e.x Google, Kakatalk).
 *
 * <p>
 * Client should provide this token when 3rd party logging-in (a.k.a social login).
 * Once validating this token with its 3rd-party service successfully,
 * then user is logged into our service (a new token will be generated and returned
 * by our service).
 * </p> */
@Value.Immutable
@JsonDeserialize(as = ImmutableSocialIdToken.class)
public interface SocialIdToken {
  String getToken();

}