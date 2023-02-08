package chorestogetherapiservice.exception.sociallogin;

/** Exception for ID token validation failure or unexpected behavior occurred during validation. */
public class SocialIdTokenValidationExecption extends RuntimeException {
  public SocialIdTokenValidationExecption(String message) {
    super(message);
  }
}
