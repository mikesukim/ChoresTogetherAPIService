package chorestogetherapiservice.datastore

import chorestogetherapiservice.TestingConstant
import chorestogetherapiservice.domain.ImmutableToken
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

class UserItemSpec extends Specification {

    def emailMock = TestingConstant.EMAIL
    def instantMock = GroovyMock(Instant.class)
    def rawTokenMock = "token"
    def uidMock = "uid"

    @Shared
    def user = TestingConstant.USER

    @Subject
    UserItem userItem = new UserItemBuilder().email(emailMock).registrationDate(instantMock).token(rawTokenMock).uid(uidMock).build()

    def "test variables"() {
        expect:
        userItem.getEmail() == emailMock
        userItem.getToken() == rawTokenMock
        userItem.getRegistrationDate() == instantMock
    }

    def "test creating UserItem through factory method"() {
        given:
        def tokenMock = ImmutableToken.builder().token(rawTokenMock).build()

        when:
        def userItem = UserItem.of(user, tokenMock)

        then:
        userItem.getToken() == rawTokenMock
        userItem.getEmail() == emailMock
    }
}
