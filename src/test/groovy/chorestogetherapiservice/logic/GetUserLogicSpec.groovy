package chorestogetherapiservice.logic

import chorestogetherapiservice.datastore.UserDao
import chorestogetherapiservice.datastore.UserItem
import chorestogetherapiservice.datastore.UserItemBuilder
import chorestogetherapiservice.datastore.UserItemSpec
import chorestogetherapiservice.domain.ImmutableUser
import chorestogetherapiservice.domain.ImmutableUserEmail
import chorestogetherapiservice.domain.User
import chorestogetherapiservice.domain.UserEmail
import chorestogetherapiservice.exception.datastore.NoItemFoundException
import spock.lang.Specification
import spock.lang.Subject
import java.time.Instant

class GetUserLogicSpec extends Specification {

    UserDao userDaoMock = Mock(UserDao.class)

    @Subject
    GetUserLogic getUserLogic = new GetUserLogic(userDaoMock)

    def "test success getUser"() {
        given:
        def userEmail = ImmutableUserEmail.builder().email("fake@gmail.com").build()
        def userItem = new UserItemBuilder().email("fake@gmail.com").registrationDate(Instant.now()).build()
        def user = ImmutableUser.builder().email("fake@gmail.com").build()

        when:
        def result = getUserLogic.getUser(userEmail)
        def expectedResult = user

        then:
        result.getEmail() == expectedResult.getEmail()

        1 * userDaoMock.get(userEmail) >> userItem
        0 * _
    }

    def "test raising NoItemFoundException when no user was found"() {
        given:
        def userEmail = ImmutableUserEmail.builder().email("fake@gmail.com").build()

        when:
        getUserLogic.getUser(userEmail)

        then:
        thrown(NoItemFoundException)

        1 * userDaoMock.get(userEmail) >> {throw new NoItemFoundException()}
        0 * _
    }
}
