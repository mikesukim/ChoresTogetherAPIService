package chorestogetherapiservice.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/**
 * Immutable User type.
 * All user related parameters from request needs to be transformed into this type,
 * and all classes from this service use this type for type safety.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableUser.class)
@JsonDeserialize(as = ImmutableUser.class)
public interface User {
  String getEmail();
}
