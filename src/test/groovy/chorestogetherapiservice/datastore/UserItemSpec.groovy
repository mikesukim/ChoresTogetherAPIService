package chorestogetherapiservice.datastore

import chorestogetherapiservice.domain.ImmutableToken
import chorestogetherapiservice.domain.ImmutableUser
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

class UserItemSpec extends Specification {

    def emailMock = "fake@email.com"
    def instantMock = GroovyMock(Instant.class)
    def rawTokenMock = "token"

    @Subject
    UserItem userItem = new UserItemBuilder().email(emailMock).registrationDate(instantMock).token(rawTokenMock).build()

    def "test variables"() {
        expect:
        userItem.getEmail() == emailMock
        userItem.getToken() == rawTokenMock
        userItem.getRegistrationDate() == instantMock
    }

    def "test creating UserItem through factory method"() {
        given:
        def userMock = ImmutableUser.builder().email(emailMock).build()
        def tokenMock = ImmutableToken.builder().token(rawTokenMock).build()

        when:
        def userItem = UserItem.of(userMock, tokenMock)

        then:
        userItem.getToken() == rawTokenMock
        userItem.getEmail() == emailMock
    }
}
