package chorestogetherapiservice.domain

import spock.lang.Specification
import spock.lang.Subject

class TokenSpec extends Specification {

    @Subject
    Token token

    def "test successful constructing ImmutableToken"() {
        when:
        def rawTokenMock = "rawToken"
        token = ImmutableToken.builder().token(rawTokenMock).build()

        then:
        token.getToken() == rawTokenMock
    }
}
