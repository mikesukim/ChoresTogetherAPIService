package chorestogetherapiservice.domain;

import chorestogetherapiservice.datastore.UserItem;
import chorestogetherapiservice.util.StringPatternCheckUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;
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
  //TODO: add uid
  String getEmail();

  /**
   *  Before creating immutables, check if attributes are in correct format.
   *  if not, cancel building immutable and raise error.
   *  <a href="https://immutables.github.io/immutable.html#precondition-check-method">more info</a>
   */
  @Value.Check
  default void check() {
    Preconditions.checkState(
        StringPatternCheckUtil.checkEmailPattern(getEmail()),
        "email format is not correct");
  }


  /**
   * Expressive factory methods fpr conversion of UserItem type to User.
   * <a href="https://immutables.github.io/immutable.html#expressive-factory-methods">doc</a>
   * */
  static User of(UserItem userItem) {
    return ImmutableUser.builder()
        .email(userItem.getEmail())
        .build();
  }
}
