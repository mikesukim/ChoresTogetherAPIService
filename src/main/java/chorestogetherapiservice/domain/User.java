package chorestogetherapiservice.domain;

/**
 * Immutable User type.
 * All user related parameters from request needs to be transformed into this type,
 * and all classes from this service use this type for type safety.
 */
public class User {
  //TODO: create User Immutable class using Immutable library

  private final String email;

  public User(String email) {
    this.email = email;
  }

  public String getEmail() {
    return this.email;
  }
}
