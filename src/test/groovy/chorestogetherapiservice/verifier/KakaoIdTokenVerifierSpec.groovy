package chorestogetherapiservice.verifier

import chorestogetherapiservice.domain.SocialIdToken
import spock.lang.Specification
import spock.lang.Subject


class KakaoIdTokenVerifierSpec extends Specification {

    def socialIdTokenMock = Mock(SocialIdToken.class)

    @Subject
    def kakaoIdTokenVerifier = new KakaoIdTokenVerifier()


    def "test verification token"() {
        when:
        def result = kakaoIdTokenVerifier.verify(socialIdTokenMock)

        then:
        result == Optional.empty()
    }
}
