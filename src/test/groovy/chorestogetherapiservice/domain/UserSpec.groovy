package chorestogetherapiservice.domain

import spock.lang.Specification
import spock.lang.Subject

class UserSpec extends Specification {

    def email = "fake@email.com"

    @Subject
    User user = new User(email)

    def "test getEmail"() {
        when:
        def result = user.getEmail()

        then:
        result == email
    }
}
