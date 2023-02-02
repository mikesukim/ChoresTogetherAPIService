package chorestogetherapiservice.domain

import spock.lang.Specification
import spock.lang.Subject

class SocialIdTokenSpec extends Specification {

    @Subject
    SocialIdToken token

    def "test successful constructing ImmutableToken"() {
        when:
        def rawTokenMock = "rawToken"
        token = ImmutableSocialIdToken.builder().token(rawTokenMock).build()

        then:
        token.getToken() == rawTokenMock
    }
}
