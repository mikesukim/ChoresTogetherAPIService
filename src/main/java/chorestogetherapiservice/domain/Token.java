package chorestogetherapiservice.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/** Immutable Token type. */
@Value.Immutable
@JsonSerialize(as = ImmutableToken.class)
public interface Token {
  String getToken();
}
