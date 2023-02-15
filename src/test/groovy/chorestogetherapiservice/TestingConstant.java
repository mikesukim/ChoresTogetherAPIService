package chorestogetherapiservice;

import chorestogetherapiservice.domain.ImmutableUser;
import chorestogetherapiservice.domain.User;

/** constants for testing. */
public final class TestingConstant {

  static final String EMAIL = "someEmail@email.com";
  static final User USER = ImmutableUser.builder().email(EMAIL).build();
}
