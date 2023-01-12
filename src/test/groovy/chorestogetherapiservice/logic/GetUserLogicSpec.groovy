package chorestogetherapiservice.logic

import chorestogetherapiservice.datastore.UserDao
import chorestogetherapiservice.datastore.UserItem
import chorestogetherapiservice.datastore.UserItemSpec
import chorestogetherapiservice.domain.ImmutableUserEmail
import chorestogetherapiservice.domain.User
import chorestogetherapiservice.domain.UserEmail
import chorestogetherapiservice.exception.datastore.NoItemFoundException
import spock.lang.Specification
import spock.lang.Subject

class GetUserLogicSpec extends Specification {

    UserDao userDaoMock = Mock(UserDao.class)

    @Subject
    GetUserLogic getUserLogic = new GetUserLogic(userDaoMock)

    def "test success getUser"() {
        given:
        def userEmail = ImmutableUserEmail.builder().email("fake@gmail.com").build()
        def userItem = new UserItem()
        userItem.setEmail("fake@gmail.com")
        def user = ImmutableUserEmail.builder().email("fake@gmail.com").build()

        when:
        def result = getUserLogic.getUser(userEmail)
        def expectedResult = user

        then:
        result.getEmail() == expectedResult.getEmail()

        1 * userDaoMock.get(userEmail) >> Optional<UserItem>.of(userItem)
        0 * _
    }

    def "test raising NoItemFoundException when no user was found"() {
        given:
        def userEmail = ImmutableUserEmail.builder().email("fake@gmail.com").build()
        def userItem = new UserItem()
        userItem.setEmail("fake@gmail.com")

        when:
        getUserLogic.getUser(userEmail)

        then:
        thrown(NoItemFoundException)

        1 * userDaoMock.get(userEmail) >> Optional<UserItemSpec>.empty()
        0 * _
    }
}
