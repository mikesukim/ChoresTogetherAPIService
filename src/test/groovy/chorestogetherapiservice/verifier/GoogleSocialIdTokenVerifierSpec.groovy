package chorestogetherapiservice.verifier

import chorestogetherapiservice.domain.SocialIdToken
import chorestogetherapiservice.exception.sociallogin.SocialIdTokenValidationExecption
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import spock.lang.Specification
import spock.lang.Subject

import java.security.GeneralSecurityException


class GoogleSocialIdTokenVerifierSpec extends Specification {

    def socialIdTokenMock = Mock(SocialIdToken.class)
    def googleIdTokenVerifierMock = Mock(GoogleIdTokenVerifier.class)
    def googleIdTokenMock = Mock(GoogleIdToken.class)
    def googleIdTokenPayloadMock = Mock(GoogleIdToken.Payload.class)

    @Subject
    def googleSocialIdTokenVerifier = new GoogleSocialIdTokenVerifier(googleIdTokenVerifierMock)


    def "test verification token success"() {
        given:
        def userEmail = "fake@email.com"

        when:
        def result = googleSocialIdTokenVerifier.verify(socialIdTokenMock)

        then:
        result == Optional.of(userEmail)

        1 * socialIdTokenMock.getToken() >> userEmail
        1 * googleIdTokenVerifierMock.verify(userEmail) >> googleIdTokenMock
        1 * googleIdTokenMock.getPayload() >> googleIdTokenPayloadMock
        1 * googleIdTokenPayloadMock.getEmail() >> userEmail
        0 * _
    }

    def "test verification token failed"() {
        given:
        def userEmail = "fake@email.com"

        when:
        googleSocialIdTokenVerifier.verify(socialIdTokenMock)

        then:
        thrown(SocialIdTokenValidationExecption)

        1 * socialIdTokenMock.getToken() >> userEmail
        1 * googleIdTokenVerifierMock.verify(userEmail) >> null
        0 * _
    }

    def "test exception occurred during validation by GoogleIdTokenVerifier "() {
        given:
        def userEmail = "fake@email.com"

        when:
        googleSocialIdTokenVerifier.verify(socialIdTokenMock)

        then:
        thrown(SocialIdTokenValidationExecption)

        1 * socialIdTokenMock.getToken() >> userEmail
        1 * googleIdTokenVerifierMock.verify(userEmail) >> {throw new GeneralSecurityException()}
        0 * _
    }
}
