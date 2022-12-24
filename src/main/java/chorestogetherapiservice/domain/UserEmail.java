package chorestogetherapiservice.domain;

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
