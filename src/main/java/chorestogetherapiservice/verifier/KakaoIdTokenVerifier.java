package chorestogetherapiservice.verifier;

import chorestogetherapiservice.domain.SocialIdToken;
import java.util.Optional;
import javax.inject.Singleton;

/** This verifies Kakao's ID_TOKEN with Kakao's library.
 * <a href="https://developers.kakao.com/docs/latest/en/kakaologin/rest-api#login-with-oidc">
 *   doc</a>*/
@Singleton
public class KakaoIdTokenVerifier implements SocialIdTokenVerifier {
  @Override
  public Optional<String> verify(SocialIdToken socialIdToken) {
    //TODO : implement the logic
    return Optional.empty();
  }
}
