package chorestogetherapiservice.domain;

import chorestogetherapiservice.util.StringPatternCheckUtil;
import com.google.common.base.Preconditions;
import org.immutables.value.Value;

/**
 * Immutable UserEmail type.
 * user's email parameter from request needs to be transformed into this type,
 * and all classes from this service use this type of type safety.
 */

@Value.Immutable
public interface UserEmail {
  String getEmail();

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @Value.Check
  default void check() {
    Preconditions.checkState(
        StringPatternCheckUtil.checkEmailPattern(getEmail()),
        "email format is not correct");
  }
}
