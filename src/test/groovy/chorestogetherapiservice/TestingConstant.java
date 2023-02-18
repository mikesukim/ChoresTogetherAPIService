package chorestogetherapiservice;

import chorestogetherapiservice.datastore.UserItem;
import chorestogetherapiservice.datastore.UserItemBuilder;
import chorestogetherapiservice.domain.ImmutableUser;
import chorestogetherapiservice.domain.User;
import java.time.Instant;

/** constants for testing. */
public final class TestingConstant {

  static final String EMAIL = "someEmail@email.com";
  static final String UID = "123";
  static final String TOKEN = "token";
  static final Instant INSTANT = Instant.ofEpochSecond(1);
  static final User USER = ImmutableUser.builder().email(EMAIL).build();

  static final UserItem USER_ITEM = new UserItemBuilder()
      .email(EMAIL).uid(UID).token(TOKEN).registrationDate(INSTANT).build();
  static final UserItem USER_ITEM_2 = new UserItemBuilder()
      .email(EMAIL).uid(UID).token(TOKEN).registrationDate(INSTANT).build();
}
