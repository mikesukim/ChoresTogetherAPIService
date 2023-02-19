package chorestogetherapiservice.domain

import spock.lang.Specification
import spock.lang.Subject

class UserSpec extends Specification {

    def email = "fake@email.com"
    def uid = "123"

    @Subject
    User user = ImmutableUser.builder().email(email).uid(uid).build()

    def "test getEmail"() {
        when:
        def result = user.getEmail()

        then:
        result == email
    }
}
