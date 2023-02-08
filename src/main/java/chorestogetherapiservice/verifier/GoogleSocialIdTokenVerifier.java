package chorestogetherapiservice.verifier;

import chorestogetherapiservice.domain.SocialIdToken;
import chorestogetherapiservice.exception.sociallogin.SocialIdTokenValidationExecption;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Optional;

/** This verifies Google's ID_TOKEN with Google's library.
 * <a href="https://developers.google.com/identity/sign-in/web/backend-auth">doc</a>*/
@Singleton
public class GoogleSocialIdTokenVerifier implements SocialIdTokenVerifier {

  GoogleIdTokenVerifier googleIdTokenVerifier;

  @Inject
  public GoogleSocialIdTokenVerifier(GoogleIdTokenVerifier googleIdTokenVerifier) {
    this.googleIdTokenVerifier = googleIdTokenVerifier;
  }

  @Override
  public Optional<String> verify(SocialIdToken socialIdToken)
      throws SocialIdTokenValidationExecption {
    // https://developers.google.com/identity/gsi/web/guides/verify-google-id-token
    GoogleIdToken idToken = null;
    try {
      idToken = googleIdTokenVerifier.verify(socialIdToken.getToken());
    } catch (Exception e) {
      throw new SocialIdTokenValidationExecption(e.getMessage());
    }
    if (idToken == null) {
      throw new SocialIdTokenValidationExecption("idToken validation is failed");
    }

    GoogleIdToken.Payload payload = idToken.getPayload();
    return Optional.of(payload.getEmail());
  }
}
