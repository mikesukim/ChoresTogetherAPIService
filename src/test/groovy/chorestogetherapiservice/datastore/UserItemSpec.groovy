package chorestogetherapiservice.datastore

import chorestogetherapiservice.domain.UserEmail
import spock.lang.Specification
import spock.lang.Subject

class UserItemSpec extends Specification {

    def email = "fake@email.com"

    @Subject
    UserItem userItem = new UserItem()

    def "test setEmail"() {
        when:
        userItem.setEmail(email)

        then:
        userItem.getEmail() == email
    }
}
