package chorestogetherapiservice.domain;

import org.immutables.value.Value;

/**
 * Immutable UserEmail type.
 * user's email parameter from request needs to be transformed into this type,
 * and all classes from this service use this type of type safety.
 */

@Value.Immutable
public interface UserEmail {
  String getEmail();
}
