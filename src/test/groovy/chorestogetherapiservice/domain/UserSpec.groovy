package chorestogetherapiservice.domain

import spock.lang.Specification
import spock.lang.Subject

class UserSpec extends Specification {

    def email = "fake@email.com"

    @Subject
    User user = ImmutableUser.builder().email(email).build()

    def "test getEmail"() {
        when:
        def result = user.getEmail()

        then:
        result == email
    }
}
