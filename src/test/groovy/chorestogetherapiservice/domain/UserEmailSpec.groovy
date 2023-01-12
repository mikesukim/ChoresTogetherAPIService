package chorestogetherapiservice.domain

import spock.lang.Specification
import spock.lang.Subject

class UserEmailSpec extends Specification {

    def email = "fake@email.com"

    @Subject
    def userEmail = ImmutableUserEmail.builder().email(email).build()

    def "test getEmail"() {
        when:
        def result = userEmail.getEmail()

        then:
        result == email
    }
}
