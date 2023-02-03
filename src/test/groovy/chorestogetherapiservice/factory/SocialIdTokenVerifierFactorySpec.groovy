package chorestogetherapiservice.factory

import chorestogetherapiservice.domain.SocialLoginServiceType
import chorestogetherapiservice.verifier.GoogleIdTokenVerifier
import chorestogetherapiservice.verifier.KakaoIdTokenVerifier
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class SocialIdTokenVerifierFactorySpec extends Specification {

    @Shared
    def googleIdTokenVerifierMock = Mock(GoogleIdTokenVerifier.class)
    @Shared
    def kakaoIdTokenVerifierMock = Mock(KakaoIdTokenVerifier.class)

    @Subject
    def socialIdTokenVerifierFactory = new SocialIdTokenVerifierFactory(
            googleIdTokenVerifierMock,kakaoIdTokenVerifierMock)

    def "test getSocialIdTokenVerifier success"() {

        when:
        def result = socialIdTokenVerifierFactory.getSocialIdTokenVerifier(socialLoginServiceType)

        then:
        result == expectedResult

        where:
        socialLoginServiceType              |   expectedResult
        SocialLoginServiceType.GOOGLE       |   googleIdTokenVerifierMock
        SocialLoginServiceType.KAKAO        |   kakaoIdTokenVerifierMock
    }

    def "test getSocialIdTokenVerifier error"() {
        when:
        socialIdTokenVerifierFactory.getSocialIdTokenVerifier(SocialLoginServiceType.NOT_IMPLEMENTED_SOCIAL_SERVICE)

        then:
        thrown(RuntimeException)
    }
}
