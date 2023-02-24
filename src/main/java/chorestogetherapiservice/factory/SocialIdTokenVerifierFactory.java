package chorestogetherapiservice.factory;

import chorestogetherapiservice.domain.SocialLoginServiceType;
import chorestogetherapiservice.verifier.GoogleSocialIdTokenVerifier;
import chorestogetherapiservice.verifier.KakaoIdTokenVerifier;
import chorestogetherapiservice.verifier.SocialIdTokenVerifier;
import javax.inject.Inject;
import javax.inject.Singleton;

/** Factory for returning SocialIdTokenVerifier implementation based on socialLoginServiceType. */
@Singleton
public class SocialIdTokenVerifierFactory {

  GoogleSocialIdTokenVerifier googleSocialIdTokenVerifier;
  KakaoIdTokenVerifier kakaoIdTokenVerifier;

  @Inject
  public SocialIdTokenVerifierFactory(GoogleSocialIdTokenVerifier googleSocialIdTokenVerifier,
                                      KakaoIdTokenVerifier kakaoIdTokenVerifier) {
    this.googleSocialIdTokenVerifier = googleSocialIdTokenVerifier;
    this.kakaoIdTokenVerifier = kakaoIdTokenVerifier;
  }

  /** returns SocialIdTokenVerifier implementation based on socialLoginServiceType. */
  public SocialIdTokenVerifier getSocialIdTokenVerifier(
      SocialLoginServiceType socialLoginServiceType) {
    switch (socialLoginServiceType) {
      case GOOGLE:
        return googleSocialIdTokenVerifier;
      case KAKAO:
        return kakaoIdTokenVerifier;
      default:
        throw new RuntimeException("not supported social login is used.");
    }
  }

}
