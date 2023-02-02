package chorestogetherapiservice.factory;

import chorestogetherapiservice.domain.SocialLoginServiceType;
import chorestogetherapiservice.verifier.GoogleIdTokenVerifier;
import chorestogetherapiservice.verifier.KakaoIdTokenVerifier;
import chorestogetherapiservice.verifier.SocialIdTokenVerifier;
import javax.inject.Inject;
import javax.inject.Singleton;

/** Factory for returning SocialIdTokenVerifier implementation based on socialLoginServiceType. */
@Singleton
public class SocialIdTokenVerifierFactory {

  GoogleIdTokenVerifier googleIdTokenVerifier;
  KakaoIdTokenVerifier kakaoIdTokenVerifier;

  @Inject
  public SocialIdTokenVerifierFactory(GoogleIdTokenVerifier googleIdTokenVerifier,
                                      KakaoIdTokenVerifier kakaoIdTokenVerifier) {
    this.googleIdTokenVerifier = googleIdTokenVerifier;
    this.kakaoIdTokenVerifier = kakaoIdTokenVerifier;
  }

  /** returns SocialIdTokenVerifier implementation based on socialLoginServiceType. */
  public SocialIdTokenVerifier getSocialIdTokenVerifier(
      SocialLoginServiceType socialLoginServiceType) {
    switch (socialLoginServiceType) {
      case GOOGLE:
        return googleIdTokenVerifier;
      case KAKAO:
        return kakaoIdTokenVerifier;
      default:
        // TODO: test coverage
        throw new RuntimeException("not supported social login is used.");
    }
  }

}
