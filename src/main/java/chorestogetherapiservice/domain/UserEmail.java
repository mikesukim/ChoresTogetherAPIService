package chorestogetherapiservice.domain;

/**
 * Immutable UserEmail type.
 * user's email parameter from request needs to be transformed into this type,
 * and all classes from this service use this type of type safety.
 */
public class UserEmail {
  //TODO: create UserEmail Immutable class using Immutable library
  private final String email;

  public UserEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }
}
