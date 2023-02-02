package chorestogetherapiservice.verifier

import chorestogetherapiservice.domain.SocialIdToken
import spock.lang.Specification
import spock.lang.Subject


class GoogleIdTokenVerifierSpec extends Specification {

    def socialIdTokenMock = Mock(SocialIdToken.class)

    @Subject
    def googleIdTokenVerifier = new GoogleIdTokenVerifier()


    def "test verification token"() {
        when:
        def result = googleIdTokenVerifier.verify(socialIdTokenMock)

        then:
        result == Optional.empty()
    }
}
