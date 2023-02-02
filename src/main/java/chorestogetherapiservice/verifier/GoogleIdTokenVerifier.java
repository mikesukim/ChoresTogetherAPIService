package chorestogetherapiservice.verifier;

import chorestogetherapiservice.domain.SocialIdToken;
import com.google.inject.Singleton;
import java.util.Optional;

/** This verifies Google's ID_TOKEN with Google's library.
 * <a href="https://developers.google.com/identity/sign-in/web/backend-auth">doc</a>*/
@Singleton
public class GoogleIdTokenVerifier implements SocialIdTokenVerifier {
  @Override
  public Optional<String> verify(SocialIdToken socialIdToken) {
    //TODO : implement the logic
    return Optional.empty();
  }
}
