package chorestogetherapiservice.datastore

import spock.lang.Specification
import spock.lang.Subject

class UserItemSpec extends Specification {

    def email = "fake@email.com"

    @Subject
    UserItem userItem = new UserItemBuilder().email(email).build()

    def "test getEmail"() {
        userItem.getEmail() == email
    }
}
