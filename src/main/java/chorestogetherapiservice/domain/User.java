package chorestogetherapiservice.domain;

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
